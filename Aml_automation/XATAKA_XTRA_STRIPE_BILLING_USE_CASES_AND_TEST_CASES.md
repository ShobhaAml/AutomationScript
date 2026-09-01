# MO_2645 Stripe Billing Xataka Xtra - Use Cases and Test Cases

Source document: `MO_2645 - Integracion Stripe Billing Xataka Xtra`

## Scope

- Product: Xataka Xtra subscriptions.
- Page type: Xataka landing page, `https://www.xataka.com/xtra`.
- Platform: Web.
- Site to deploy: Xataka.
- Payment provider added: Stripe Billing.
- Existing provider retained for first phase: PayPal.
- Checkout model: Stripe-hosted Checkout at `checkout.stripe.com`.
- Billing portal model: Stripe Customer Portal generated per click through backend `billing_portal/sessions`.

## Out of Scope / Notes

- No custom card-payment UI is built or maintained by Xataka for the MVP.
- Stripe owns checkout form validation, card-data handling and PCI compliance.
- The document does not include Sketch or prototype links.

## Use Cases

| ID | Area | Actor | Use case |
| --- | --- | --- | --- |
| UC-STR-001 | Subscription checkout | Visitor | Subscribe to Xataka Xtra using Stripe. |
| UC-STR-002 | Subscription checkout | Visitor | Select a monthly billing plan. |
| UC-STR-003 | Subscription checkout | Visitor | Select an annual billing plan. |
| UC-STR-004 | Subscription checkout | Visitor | Pay for a subscription by credit or debit card. |
| UC-STR-005 | Subscription checkout | Logged-in visitor | See email pre-filled in Stripe Checkout when already logged in. |
| UC-STR-006 | Subscription checkout | Visitor | Enter and validate card number, expiry and CVC on Stripe Checkout. |
| UC-STR-007 | Subscription checkout | Visitor | See a clear order summary with plan, price, billing frequency and trial when applicable. |
| UC-STR-008 | Subscription checkout | Visitor | Apply a discount or promo code during checkout when enabled. |
| UC-STR-009 | Subscription checkout | Visitor | Return to Xataka welcome/success page after successful Stripe payment. |
| UC-STR-010 | Subscription checkout | Visitor | Return to Xataka subscription page after canceling Stripe Checkout. |
| UC-STR-011 | Subscription lifecycle | Subscriber | Automatically renew an active Stripe subscription. |
| UC-STR-012 | Subscription lifecycle | Subscriber | Cancel a Stripe subscription. |
| UC-STR-013 | Premium access | Subscriber | Access premium Xtra content while Stripe subscription is active. |
| UC-STR-014 | Premium access | Visitor or inactive subscriber | Be blocked from premium Xtra content when no active subscription exists. |
| UC-STR-015 | Free trial | Eligible visitor | Start a configurable free trial and receive premium access immediately. |
| UC-STR-016 | Free trial | Trial subscriber | See next renewal date equal to the trial end date. |
| UC-STR-017 | Free trial | Trial subscriber | Convert automatically to paid subscription when trial ends unless canceled. |
| UC-STR-018 | Free trial | Trial subscriber | Cancel before trial ends to avoid conversion to paid subscription. |
| UC-STR-019 | Free trial | Returning user | Be eligible for a free trial only once even after account deactivation/reactivation. |
| UC-STR-020 | Duplicate trial prevention | User reusing card | Be prevented from receiving another trial with a card/payment method already used for trial. |
| UC-STR-021 | Discount coupons | Visitor | Apply percentage discount codes. |
| UC-STR-022 | Discount coupons | Visitor | Apply fixed amount discount codes. |
| UC-STR-023 | Discount coupons | Visitor | Apply one-time discount codes. |
| UC-STR-024 | Discount coupons | Visitor | Apply recurring discount codes. |
| UC-STR-025 | Discount coupons | Visitor | Be blocked from using expired promo codes. |
| UC-STR-026 | Discount coupons | Visitor | Be blocked from using promo codes after redemption limit is reached. |
| UC-STR-027 | Provider coexistence | Existing PayPal subscriber | Continue using PayPal during first phase without disruption. |
| UC-STR-028 | Provider coexistence | New subscriber | Use Stripe as the primary subscription infrastructure for new subscriptions. |
| UC-STR-029 | Invoice management | Stripe subscriber | View Stripe-specific invoice messaging on `/gestionar-suscripcion/facturas`. |
| UC-STR-030 | Invoice management | Stripe subscriber | Open Stripe Customer Portal from the invoices page. |
| UC-STR-031 | Invoice management | PayPal subscriber | See PayPal-specific invoice messaging when active provider is PayPal. |
| UC-STR-032 | Invoice management | Subscriber | View and download invoices from the provider associated with the active subscription. |
| UC-STR-033 | Analytics | Marketing/Analytics team | Preserve UTM, organic, social and referral attribution through Stripe Checkout. |
| UC-STR-034 | Analytics | Marketing/Analytics team | Preserve original attribution when user interrupts checkout to authenticate with Google SSO. |
| UC-STR-035 | Webhooks | Backend system | Synchronize Stripe subscription state from webhook events. |
| UC-STR-036 | Webhooks | Backend system | Retry webhook failures safely without duplicate subscriptions. |
| UC-STR-037 | Stripe catalog | Operations team | Manage products, prices, customers, subscriptions, coupons and promotion codes in Stripe Billing. |
| UC-STR-038 | Error handling | Visitor | See localized payment error message when card payment fails. |
| UC-STR-039 | Error handling | Backend system | Keep subscription inactive when payment fails. |
| UC-STR-040 | Communications | Subscriber | Receive no welcome screens or emails when duplicate-trial prevention cancels activation. |

## Test Cases

### 1. Stripe Checkout Entry and Plan Selection

| ID | Requirement | Preconditions | Steps | Expected result |
| --- | --- | --- | --- | --- |
| TC-STR-001 | Stripe subscription CTA | Xataka Xtra landing page is available. | Open `https://www.xataka.com/xtra` and click the subscription CTA for Stripe. | User is redirected to Stripe Checkout on `checkout.stripe.com`. |
| TC-STR-002 | Stripe coexists with PayPal | First phase rollout is active. | Inspect available payment/subscription paths for existing PayPal users and new users. | PayPal remains available for existing subscribers; Stripe is available for new subscription checkout. |
| TC-STR-003 | Monthly plan selection | Monthly plan is configured in Stripe. | Choose monthly plan from Xataka Xtra landing page and proceed to checkout. | Stripe Checkout order summary shows monthly plan name, monthly price and monthly billing frequency. |
| TC-STR-004 | Annual plan selection | Annual plan is configured in Stripe. | Choose annual plan from Xataka Xtra landing page and proceed to checkout. | Stripe Checkout order summary shows annual plan name, annual price and annual billing frequency. |
| TC-STR-005 | Correct Stripe price mapping | Monthly and annual Stripe prices exist. | Start checkout for each plan and inspect Stripe session/summary. | Each plan maps to the correct Stripe Price ID and displayed amount. |
| TC-STR-006 | Hosted checkout only | Stripe MVP is enabled. | Start checkout and inspect payment page domain and UI. | Payment UI is hosted by Stripe; Xataka does not render or collect card fields locally. |
| TC-STR-007 | Logged-in email prefill | User is logged in with a known email. | Start Stripe checkout. | Email field is pre-filled with logged-in user email. |
| TC-STR-008 | Anonymous checkout email | User is not logged in. | Start Stripe checkout. | Checkout allows user to enter email or follows configured authentication flow. |
| TC-STR-009 | Order summary includes trial | Selected plan has active trial. | Start checkout for that plan. | Stripe Checkout summary shows plan name, price, billing frequency and trial period. |
| TC-STR-010 | Order summary without trial | Selected plan has no active trial. | Start checkout for that plan. | Stripe Checkout summary shows plan name, price and billing frequency without trial messaging. |
| TC-STR-011 | Cancel checkout redirects back | User is on Stripe Checkout. | Click cancel/back action configured by Stripe. | User returns to Xataka subscription page. |
| TC-STR-012 | Successful checkout redirects welcome | Valid payment succeeds. | Complete Stripe Checkout. | User returns to Xataka success/welcome page. |
| TC-STR-013 | Success page associates account | Valid payment succeeds for a user account. | Return from Stripe success flow and inspect account subscription state. | Xataka account is associated with active Stripe subscription/customer. |
| TC-STR-014 | Prevent duplicate checkout activation | User refreshes or revisits success URL. | Complete checkout, then reload success URL. | No duplicate subscription or duplicate activation is created. |

### 2. Card Payment Method and Payment Errors

| ID | Requirement | Preconditions | Steps | Expected result |
| --- | --- | --- | --- | --- |
| TC-PAY-001 | Credit card payment | Stripe test card for successful credit payment is available. | Complete checkout with credit card. | Payment succeeds and subscription activates. |
| TC-PAY-002 | Debit card payment | Stripe test card for successful debit payment is available. | Complete checkout with debit card. | Payment succeeds and subscription activates. |
| TC-PAY-003 | Card number validation | User is on Stripe Checkout. | Enter incomplete/invalid card number. | Stripe shows real-time validation error. |
| TC-PAY-004 | Expiry validation | User is on Stripe Checkout. | Enter invalid or expired card expiry. | Stripe shows real-time validation error. |
| TC-PAY-005 | CVC validation | User is on Stripe Checkout. | Enter invalid/incomplete CVC. | Stripe shows real-time validation error. |
| TC-PAY-006 | Localized decline message | Stripe decline test card is available. | Complete checkout with declined card. | User sees payment error message in user's language. |
| TC-PAY-007 | Subscription not active after failed payment | Payment fails during checkout. | Return to Xataka/account state after failed checkout. | Subscription is not activated and premium content remains blocked. |
| TC-PAY-008 | Stripe retry logic | Payment fails for a case where Stripe retry applies. | Trigger failed payment/renewal scenario. | Stripe retry behavior applies according to Stripe Billing configuration. |
| TC-PAY-009 | No card data stored locally | Payment attempt is made through Stripe Checkout. | Inspect Xataka requests/logs/database for card number, expiry and CVC. | Xataka does not receive or store raw card data. |

### 3. Free Trials

| ID | Requirement | Preconditions | Steps | Expected result |
| --- | --- | --- | --- | --- |
| TC-TRL-001 | Seven-day trial configuration | Plan configured with 7-day trial. | Start checkout for plan. | Checkout and subscription details reflect 7-day trial. |
| TC-TRL-002 | Fourteen-day trial configuration | Plan configured with 14-day trial. | Start checkout for plan. | Checkout and subscription details reflect 14-day trial. |
| TC-TRL-003 | Thirty-day trial configuration | Plan configured with 30-day trial. | Start checkout for plan. | Checkout and subscription details reflect 30-day trial. |
| TC-TRL-004 | Trial configurable per plan | Monthly and annual plans have different trial settings. | Start checkout for each plan. | Each plan uses its own configured trial duration. |
| TC-TRL-005 | Trial immediate access | Eligible user completes checkout for trial plan. | Return to Xataka and open premium content. | User has full Xtra access immediately. |
| TC-TRL-006 | No charge during trial | Trial starts successfully. | Inspect Stripe invoice/payment state during trial. | No charge is generated until trial ends. |
| TC-TRL-007 | Renewal date equals trial end | Trial starts successfully. | Open subscription details page. | Next renewal date is the exact trial end date. |
| TC-TRL-008 | Trial converts to paid | Trial subscription reaches trial end without cancellation. | Advance to/trigger trial end in Stripe test environment. | Subscription converts automatically to paid billing cycle. |
| TC-TRL-009 | Cancel before trial end | Trial subscription is active. | Cancel subscription before trial end. | Subscription does not convert to paid after trial end. |
| TC-TRL-010 | One trial per account | User already redeemed a free trial. | Attempt to start another free trial with same account. | Free trial is not granted again. |
| TC-TRL-011 | Reactivation does not reset trial eligibility | User redeemed trial, deactivated account, then reactivated. | Attempt to start another free trial. | User remains ineligible for another free trial. |
| TC-TRL-012 | Duplicate trial by card fingerprint | A payment method was previously used to redeem a free trial. | Attempt a new free trial using the same payment method from another account. | Duplication is detected after Stripe processing; subscription is canceled. |
| TC-TRL-013 | Duplicate trial refund | Duplicate card trial generated a charge. | Trigger duplicate trial prevention. | Refund is issued if a charge was generated. |
| TC-TRL-014 | Duplicate trial frontend error | Duplicate trial is detected. | Observe return page after Stripe processing. | Welcome page is not shown; frontend displays error that payment method was already used for a free trial and prompts contacting support. |
| TC-TRL-015 | Duplicate trial no activation | Duplicate trial is detected. | Inspect subscription/account state. | Subscription is not activated. |
| TC-TRL-016 | Duplicate trial no welcome communications | Duplicate trial is detected. | Inspect screen and email/event communications. | No welcome screens or welcome emails are sent. |

### 4. Discounts, Coupons and Promo Codes

| ID | Requirement | Preconditions | Steps | Expected result |
| --- | --- | --- | --- | --- |
| TC-DSC-001 | Promo code field enabled | Discount codes are enabled in Stripe Checkout. | Start checkout. | Promo code field is visible. |
| TC-DSC-002 | Promo code field disabled | Discount codes are disabled. | Start checkout. | Promo code field is not visible. |
| TC-DSC-003 | Percentage discount | Valid percentage promo code exists. | Enter promo code in Stripe Checkout. | Discount applies as configured, e.g. 20% off first month. |
| TC-DSC-004 | Fixed amount discount | Valid fixed amount promo code exists. | Enter promo code in Stripe Checkout. | Fixed amount is subtracted from order total. |
| TC-DSC-005 | One-time discount | One-time promo code exists. | Subscribe with code and inspect first and later invoices. | Discount applies once only. |
| TC-DSC-006 | Recurring discount | Recurring promo code exists. | Subscribe with code and inspect recurring invoices. | Discount applies according to recurring coupon configuration. |
| TC-DSC-007 | Annual-plan discount | Annual promo code such as 50% off annual exists. | Start annual checkout and apply code. | Discount applies to annual plan. |
| TC-DSC-008 | Campaign promo code | Campaign code such as `XTRA2026` exists. | Apply campaign code in checkout. | Stripe accepts and applies campaign code. |
| TC-DSC-009 | Expired promo code | Promo code has expiration date in the past. | Enter expired code in checkout. | Stripe rejects code and order total is unchanged. |
| TC-DSC-010 | Redemption limit reached | Promo code redemption count is exhausted. | Enter code in checkout. | Stripe rejects code and order total is unchanged. |
| TC-DSC-011 | Invalid promo code | User enters unknown code. | Submit promo code in checkout. | Stripe displays invalid-code error and order total is unchanged. |
| TC-DSC-012 | Promo attribution retained | Checkout URL includes traffic attribution parameters and promo code is used. | Complete checkout. | Subscription/conversion analytics retain original attribution and applied promotion data. |

### 5. Subscription Lifecycle and Premium Access

| ID | Requirement | Preconditions | Steps | Expected result |
| --- | --- | --- | --- | --- |
| TC-LIF-001 | Active subscription unlocks premium content | Stripe subscription is active. | Log in and open premium Xtra content. | Premium content is accessible. |
| TC-LIF-002 | Inactive subscription blocks premium content | User has no active subscription. | Open premium Xtra content. | Premium content is blocked or paywalled. |
| TC-LIF-003 | Automatic monthly renewal | Monthly Stripe subscription is active. | Trigger renewal in Stripe test clock/environment. | Subscription renews and active access continues. |
| TC-LIF-004 | Automatic annual renewal | Annual Stripe subscription is active. | Trigger renewal in Stripe test clock/environment. | Subscription renews and active access continues. |
| TC-LIF-005 | Renewal failed access state | Renewal payment fails. | Trigger failed renewal. | Retry logic applies and access state follows business rules for failed payments. |
| TC-LIF-006 | User cancellation | Active Stripe subscriber opens subscription management. | Cancel subscription. | Subscription is canceled according to configured cancellation timing. |
| TC-LIF-007 | Access after cancellation effective date | Stripe subscription cancellation reaches effective date. | Open premium content. | Premium access is removed when subscription is no longer active. |
| TC-LIF-008 | PayPal subscriber unaffected | Existing PayPal subscriber exists. | Open account/subscription pages and premium content. | PayPal subscriber flow and premium access remain functional. |
| TC-LIF-009 | Stripe primary for new subscriber | New visitor starts subscription after Stripe rollout. | Subscribe from Xataka Xtra landing. | New subscription is created through Stripe Billing. |

### 6. Webhooks and Backend Synchronization

| ID | Requirement | Preconditions | Steps | Expected result |
| --- | --- | --- | --- | --- |
| TC-WHK-001 | Checkout completed webhook sync | Stripe checkout session completes. | Send/receive `checkout.session.completed` webhook. | Backend creates or updates customer/subscription and activates Xtra access. |
| TC-WHK-002 | Subscription created/updated sync | Stripe subscription lifecycle event occurs. | Send/receive subscription webhook. | Backend subscription state matches Stripe. |
| TC-WHK-003 | Renewal payment sync | Stripe renewal invoice/payment succeeds. | Send/receive invoice/payment webhook. | Backend records renewal and keeps subscription active. |
| TC-WHK-004 | Cancellation sync | Stripe cancellation event occurs. | Send/receive cancellation webhook. | Backend marks subscription canceled/inactive at correct time. |
| TC-WHK-005 | Failed payment sync | Stripe payment failure event occurs. | Send/receive failed payment webhook. | Backend does not activate or incorrectly extend subscription. |
| TC-WHK-006 | Webhook retry safety | Webhook delivery fails once. | Let Stripe retry the same event. | Event is processed safely on retry. |
| TC-WHK-007 | Webhook idempotency | Same webhook event is delivered multiple times. | Replay identical Stripe event. | No duplicate customer, subscription, invoice or activation is created. |
| TC-WHK-008 | Out-of-order webhook handling | Subscription events arrive out of order. | Replay lifecycle events in non-chronological order. | Backend final subscription state remains correct. |
| TC-WHK-009 | Duplicate trial webhook handling | Duplicate trial prevention is triggered after Stripe processing. | Process related webhook events. | Backend cancels duplicate subscription, records refund when needed and does not activate access. |

### 7. Analytics and Attribution

| ID | Requirement | Preconditions | Steps | Expected result |
| --- | --- | --- | --- | --- |
| TC-ANA-001 | UTM preservation into checkout | User lands on Xataka Xtra with UTM parameters. | Start Stripe checkout and complete payment. | Conversion funnel retains original UTM attribution. |
| TC-ANA-002 | Organic attribution preservation | User arrives via organic search. | Start and complete Stripe checkout. | Conversion is attributed to original organic source. |
| TC-ANA-003 | Social attribution preservation | User arrives from social campaign/referrer. | Start and complete Stripe checkout. | Conversion is attributed to original social source. |
| TC-ANA-004 | Referral attribution preservation | User arrives from external referral. | Start and complete Stripe checkout. | Conversion is attributed to original referral source. |
| TC-ANA-005 | Session continuity through Stripe domain | User starts checkout from Xataka. | Complete hosted Stripe Checkout and return to Xataka. | Analytics session resumes with original context after returning from Stripe. |
| TC-ANA-006 | Google SSO does not overwrite source | User lands with UTM/referrer and interrupts checkout to authenticate with Google Social Login. | Authenticate with Google, return and complete payment. | Original source is preserved and not overwritten by Google referral. |
| TC-ANA-007 | Checkout funnel events | User starts and completes checkout. | Inspect analytics events. | Funnel has complete events for subscription CTA, checkout start, payment success/cancel/failure as applicable. |
| TC-ANA-008 | Promo code analytics | User applies promo code during checkout. | Complete checkout. | Analytics/conversion data includes promo usage without losing source attribution. |
| TC-ANA-009 | Cancelled checkout analytics | User cancels checkout. | Start checkout, cancel, and return to Xataka. | Cancel event is tracked and original session/source remains available. |

### 8. Invoice Management / Facturas Page

| ID | Requirement | Preconditions | Steps | Expected result |
| --- | --- | --- | --- | --- |
| TC-INV-001 | Stripe provider conditional render | Backend returns Stripe as active provider. | Open `/gestionar-suscripcion/facturas`. | Page renders Stripe copy instead of PayPal copy. |
| TC-INV-002 | Stripe H2 text | Backend returns Stripe as active provider. | Open facturas page. | H2 is `Solicitar una factura`. |
| TC-INV-003 | Stripe paragraph text | Backend returns Stripe as active provider. | Open facturas page. | Paragraph says `Te llega un recibo por email cada vez que se realiza un cobro (alta, renovación o cambio de plan).` |
| TC-INV-004 | Stripe history link text | Backend returns Stripe as active provider. | Open facturas page. | Link text is `Revisar todo tu historial de pagos en Stripe`. |
| TC-INV-005 | Standard block unchanged | Backend returns Stripe as active provider. | Open facturas page and inspect standard block below Stripe section. | Standard block below this section remains unchanged. |
| TC-INV-006 | PayPal provider conditional render | Backend returns PayPal as active provider. | Open facturas page. | Page renders PayPal-specific messaging, not Stripe copy. |
| TC-INV-007 | No provider fallback | Backend returns no active subscription/provider. | Open facturas page. | Page handles missing provider gracefully according to subscription/account UX. |
| TC-INV-008 | Portal link creates session per click | Stripe subscriber opens facturas page. | Click Stripe payment-history link. | Frontend calls backend to create a new Stripe Billing Portal session. |
| TC-INV-009 | Portal URL not hardcoded | Stripe subscriber opens facturas page. | Inspect rendered link and network requests before click. | Stripe portal URL is not hardcoded or generated before click. |
| TC-INV-010 | Redirect to Stripe portal domain | Backend returns portal session URL. | Click Stripe history link. | User is redirected to Stripe Customer Portal on Stripe domain; portal is not embedded in iframe. |
| TC-INV-011 | Portal return URL | User exits/completes Stripe Customer Portal. | Use portal return path. | User is returned to `/gestionar-suscripcion/facturas`. |
| TC-INV-012 | Portal single-use behavior | A Stripe portal URL has already been used or expired. | Try to reuse it after use/expiration. | URL cannot be reused; next click creates a fresh backend session. |
| TC-INV-013 | Download invoices via Stripe | Stripe subscriber has invoices. | Open Stripe Customer Portal and view/download invoices. | Subscriber can view/download Stripe invoices. |
| TC-INV-014 | Download invoices via PayPal | PayPal subscriber has invoices. | Open PayPal invoice flow. | Subscriber can view/download PayPal invoices according to existing flow. |

### 9. Stripe Billing Catalog and Configuration

| ID | Requirement | Preconditions | Steps | Expected result |
| --- | --- | --- | --- | --- |
| TC-CFG-001 | Stripe product exists | Stripe environment is configured. | Inspect Stripe catalog/configuration. | Xataka Xtra product exists in Stripe Billing. |
| TC-CFG-002 | Monthly price exists | Stripe environment is configured. | Inspect Stripe product prices. | Monthly recurring price exists and is active. |
| TC-CFG-003 | Annual price exists | Stripe environment is configured. | Inspect Stripe product prices. | Annual recurring price exists and is active. |
| TC-CFG-004 | Customer creation | New user subscribes through Stripe. | Complete checkout and inspect Stripe/backend. | Stripe Customer is created and linked to Xataka user. |
| TC-CFG-005 | Subscription creation | New user completes checkout. | Inspect Stripe/backend. | Stripe Subscription is created with selected plan. |
| TC-CFG-006 | Coupon configuration | Discount campaign exists. | Inspect Stripe coupon/promotion configuration. | Coupon and promotion code are configured with correct amount/percent, duration, expiration and redemption limits. |
| TC-CFG-007 | Promotion code availability | Promo codes are enabled for checkout. | Start checkout and apply configured promotion code. | Promotion code is accepted only when valid and applicable. |

### 10. End-to-End Scenarios

| ID | Requirement | Preconditions | Steps | Expected result |
| --- | --- | --- | --- | --- |
| TC-E2E-001 | New monthly subscriber with card | Monthly plan active; user eligible. | Land on Xataka Xtra, select monthly, complete Stripe card checkout, return to Xataka, open premium content. | Subscription is active, premium access works and analytics attribution is preserved. |
| TC-E2E-002 | New annual subscriber with promo | Annual plan and promo code active; user eligible. | Land with campaign UTM, select annual, apply promo, complete checkout. | Annual subscription activates with discount and campaign attribution preserved. |
| TC-E2E-003 | Trial subscriber conversion | Trial plan active; user eligible. | Start trial, access premium content, reach trial end. | User has access during trial and converts to paid at trial end unless canceled. |
| TC-E2E-004 | Trial cancellation before conversion | Trial plan active; user eligible. | Start trial, cancel before trial end, reach trial end. | User is not charged and subscription does not convert to paid. |
| TC-E2E-005 | Failed payment checkout | Stripe decline scenario available. | Start checkout and submit declined card. | Error is shown, subscription is inactive, premium access is blocked and failure is tracked. |
| TC-E2E-006 | Duplicate trial prevention | Prior trial card fingerprint exists. | Attempt trial with reused card and complete Stripe processing. | Subscription is canceled/refunded if needed, duplicate-trial error is shown and no welcome communication is sent. |
| TC-E2E-007 | Stripe invoice portal | Stripe subscriber active with invoice history. | Open facturas page, click Stripe history link, view portal, return. | Correct Stripe copy appears, portal session is generated on click, invoices are accessible and user returns to facturas page. |
| TC-E2E-008 | PayPal subscriber coexistence | Existing PayPal subscriber active. | Open Xataka Xtra account, premium content and facturas page. | PayPal subscriber remains undisrupted and sees PayPal-specific invoice flow. |

## Regression Checklist

- Verify Stripe checkout for monthly and annual plans.
- Verify card payment success, cancellation and failure.
- Verify trial durations for 7, 14 and 30 days.
- Verify one-time trial eligibility by account and shared card fingerprint.
- Verify promo code types: percentage, fixed amount, one-time, recurring, expired and redemption-limited.
- Verify webhook retries and idempotent duplicate event handling.
- Verify attribution continuity for UTM, organic, social and referral sources.
- Verify Google SSO does not overwrite original attribution during checkout.
- Verify active/inactive subscription access to premium content.
- Verify Stripe and PayPal invoice rendering on `/gestionar-suscripcion/facturas`.
- Verify Billing Portal session generation on each click and return URL behavior.

