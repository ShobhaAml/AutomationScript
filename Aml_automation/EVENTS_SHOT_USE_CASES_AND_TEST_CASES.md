# UX_2339 EVENTS Redesign MVP Shot - Use Cases and Test Cases

Source document: `UX_2339 - [EVENTS] - [REDESIGN]-[MVP]-[SHOT]`

## Scope

- Content type: Shot.
- Product area: Events / Superevento.
- Version: Web.
- Deployment scope: all sites except XTO, XEN, XBR and XAR.
- MVP themes: Xataka and Default.
- Not included in MVP: SSC and MGG.
- Retroactive application: No.

## Roles in Scope

The Create, Edit, Listing and Delete flows apply to:

1. Director
2. Coordinador
3. Admin
4. Editor
5. SEO
6. Ecommerce
7. Colaborador

## Use Cases

| ID | Area | Actor | Use case |
| --- | --- | --- | --- |
| UC-GEN-001 | General | Visitor | See frontend shot dates formatted as relative time for recent shots and standard homepage-shot format for older shots. |
| UC-GEN-002 | General | Admin user | See admin shot dates formatted from the right source for draft and scheduled content. |
| UC-GEN-003 | General | Visitor | See frontend shot UI using the new arrow curved right outline icon and dark button style. |
| UC-GEN-004 | General | Admin user | See Admin UI using the new shot, shot add, shot discard, arrow expand and edit cancel icons. |
| UC-FE-001 | Frontend shot | Visitor | See all published shots inside a superevento page. |
| UC-FE-002 | Frontend shot | Visitor | See the author attached to each shot. |
| UC-FE-003 | Frontend shot | Visitor | Navigate from a shot author to the author page. |
| UC-FE-004 | Frontend shot | Visitor | Interact with shot content blocks the same way as equivalent post content blocks. |
| UC-FE-005 | Frontend shot | Visitor | See the publication date/time or eligible update date/time for each shot. |
| UC-FE-006 | Frontend shot | Visitor | Share a shot on Twitter using a URL anchored to that shot. |
| UC-FE-007 | Frontend shot | Logged author | Open the Admin edit page for a shot from the frontend edit link in a new tab. |
| UC-FE-008 | Frontend shot | Visitor | Reach a shot in a superevento through `/evento/{slug}-{id}#s-{idShot}`. |
| UC-FE-009 | Frontend shot | Visitor | Reach a shot in a redirected superevento through `/evento/{slug}-{id}-{n}#s-{idShot}`. |
| UC-FE-010 | Frontend shot | Visitor | Reach a shot in a directisimo through `/live/{slug}-{id}#s-{idShot}`. |
| UC-FE-011 | Frontend shot | Visitor | Reach a shot in a redirected directisimo through `/live/{slug}-{id}-{n}#s-{idShot}`. |
| UC-FE-012 | Frontend shot | Visitor | See a deleted-shot warning only when arriving through an anchor pointing to a deleted shot. |
| UC-FE-013 | Frontend shot | Visitor | See shots ordered in reverse chronological order, most recent first. |
| UC-FE-014 | Frontend pagination | Visitor | Navigate shot pages when the superevento has more than 50 published shots. |
| UC-FE-015 | Frontend calendar | Visitor | Open the calendar and jump to dates that contain shots. |
| UC-FE-016 | Frontend calendar | Visitor | Jump to the first and last shot using calendar shortcuts. |
| UC-ADM-001 | Create shot | Admin user | Create a shot in a superevento while follow-up is active. |
| UC-ADM-002 | Create shot | Admin user | Create shots using the same publication statuses as post pages: draft, scheduled and published. |
| UC-ADM-003 | Create shot | Admin user | Add text, image, video, GIF, table, graph, code and pivot blocks to a shot. |
| UC-ADM-004 | Create shot | Admin user | Create a shot before the parent superevento has been published. |
| UC-ADM-005 | Create shot | Admin user | Use Emision tab to create new content and see existing content created by any user. |
| UC-ADM-006 | Create shot | Admin user | See no shot creation form when the superevento follow-up is over. |
| UC-ADM-007 | Create shot | Admin user | Switch to the Shot form through the tab-shot control, which is selected by default. |
| UC-ADM-008 | Shot form | Admin user | Publish a shot immediately. |
| UC-ADM-009 | Shot form | Admin user | Open and cancel the scheduling form. |
| UC-ADM-010 | Shot form | Admin user | Clear unsaved form contents without creating a draft. |
| UC-ADM-011 | Shot form | Admin user | Save non-empty form contents as a draft and clear the form. |
| UC-ADM-012 | Shot form | Admin user | Receive feedback when attempting to save an empty shot as draft. |
| UC-ADM-013 | Shot form | Admin user | Preview the current shot in the superevento page in a new browser tab. |
| UC-ADM-014 | Shot form | Mobile admin user | Use the sticky main-actions toolbar while the form action buttons are off screen. |
| UC-ADM-015 | Shot form | Admin user | Insert assets at the current cursor position from toolbar-add_asset. |
| UC-ADM-016 | Shot form | Admin user | Open menu-add_asset on a new line and insert assets from that location. |
| UC-ADM-017 | Shot form | Admin user | See keyboard shortcut help and use homepage-shot editor shortcuts. |
| UC-ADM-018 | Shot form | Admin user | Upload images by drag and drop as in homepage shots. |
| UC-ADM-019 | Render shot | Admin user | Identify draft, scheduled, published, preview, pending-changes, highlighted and expanded shot renders. |
| UC-ADM-020 | Render shot | Admin user | Expand a render-shot to see full content and actions. |
| UC-ADM-021 | Render shot | Admin user | Preview, edit, send to draft or delete a render-shot according to its state. |
| UC-ADM-022 | Render groups | Admin user | See published, scheduled and draft render groups with state-specific default expansion and ordering. |
| UC-ADM-023 | Render groups | Admin user | Collapse and expand render groups and have that preference persist when returning. |
| UC-ADM-024 | Render groups | Admin user | Use go-to-date and pagination controls in the published render group. |
| UC-ADM-025 | Render groups | Admin user | Configure render groups to show all content expanded. |
| UC-ADM-026 | Render groups | Admin user | Configure render groups to show only content created by the current user. |
| UC-ADM-027 | Scheduling | Admin user | Schedule a shot with a valid date and time. |
| UC-ADM-028 | Scheduling | Admin user | Select a time slot from the superevento slots table. |
| UC-ADM-029 | Publishing | Admin user | Publish a shot and get a snackbar linking to the frontend shot anchor. |
| UC-ADM-030 | Collaboration | Admin user | See colleague snackbars when another author schedules or publishes a shot. |
| UC-ADM-031 | Search | Admin user | Search content in Emision render groups and dismiss the search. |
| UC-ADM-032 | Filtering | Admin user | Filter Emision render groups by current author and remove that filter. |
| UC-EDIT-001 | Edit shot | Admin user | Edit shot content using the same asset tools as post pages. |
| UC-EDIT-002 | Edit shot | Admin user | Send a published shot to draft. |
| UC-EDIT-003 | Edit shot | Admin user | Send a scheduled shot to draft. |
| UC-EDIT-004 | Edit shot | Admin user | Update a draft shot and keep it as draft. |
| UC-EDIT-005 | Edit shot | Admin user | Update a published shot and keep the same publication date. |
| UC-EDIT-006 | Edit shot | Admin user | Update a scheduled shot and keep or update its scheduled date. |
| UC-EDIT-007 | Edit shot | Admin user | Leave unconsolidated changes pending on published or scheduled shots. |
| UC-EDIT-008 | Edit shot | Admin user | Republish a published shot during active follow-up so its date changes. |
| UC-EDIT-009 | Edit shot | Admin user | See republish disabled when follow-up is over. |
| UC-EDIT-010 | Edit shot | Admin user | Be prevented from editing a shot currently taken by another editor. |
| UC-TAB-001 | Tabla listing | Admin user | See all scheduled and published shots in the Tabla > Publicaciones listing. |
| UC-TAB-002 | Tabla listing | Admin user | See all draft shots in the Tabla > Borradores listing. |
| UC-TAB-003 | Tabla listing | Admin user | Preview, edit and send scheduled/published shots to draft from Tabla. |
| UC-TAB-004 | Tabla listing | Admin user | Preview, edit and delete draft shots from Tabla. |
| UC-TAB-005 | Tabla listing | Admin user | Order and filter Tabla listing rows by supported columns. |
| UC-TAB-006 | Tabla listing | Admin user | Search across Publicaciones and Borradores together and dismiss the search. |
| UC-DEL-001 | Delete shot | Admin user | Delete a draft shot from Tabla after confirming the prompt. |
| UC-DEL-002 | Delete shot | Admin user | Delete a draft shot from Emision after confirming the prompt. |
| UC-DEL-003 | Delete shot | Visitor | See deleted-shot warning on frontend when visiting a deleted shot anchor. |

## Test Cases

### 0. General Requirements and UI System

| ID | Requirement | Preconditions | Steps | Expected result |
| --- | --- | --- | --- | --- |
| TC-GEN-001 | Front date: within last minute | A published shot exists with publication/update timestamp less than 60 seconds old. | Open the superevento frontend page and inspect the shot datetime. | Datetime shows `Hace {s} s` using seconds. |
| TC-GEN-002 | Front date: within last hour | A published shot exists with timestamp more than 1 minute and less than 1 hour old. | Open the superevento frontend page and inspect the shot datetime. | Datetime shows `Hace {m} min` using minutes. |
| TC-GEN-003 | Front date: older shots | A published shot exists older than one hour. | Open the superevento frontend page and inspect the shot datetime. | Datetime uses the same format as homepage shots. |
| TC-GEN-004 | Front date source after update | A published shot was updated and marked to show the date as new. | Open the superevento frontend page and inspect the updated shot datetime. | Datetime uses the eligible update date instead of the original publication date. |
| TC-GEN-005 | Front date source without update flag | A published shot was updated but not marked to show the date as new. | Open the superevento frontend page and inspect the updated shot datetime. | Datetime continues to use the publication date. |
| TC-GEN-006 | Admin date source for drafts | A draft shot exists. | Open Admin Emision/Tabla where the draft is shown. | Draft date uses latest modification date. |
| TC-GEN-007 | Admin date source for scheduled shots | A scheduled shot exists. | Open Admin Emision/Tabla where the scheduled shot is shown. | Scheduled date uses scheduled publication date. |
| TC-GEN-008 | Admin date format: yesterday | A draft or scheduled shot date is yesterday. | Inspect the admin status/date text. | Date format is `Ayer, {hh:mm}`. |
| TC-GEN-009 | Admin date format: today | A draft or scheduled shot date is today. | Inspect the admin status/date text. | Date format is `Hoy, {hh:mm}`. |
| TC-GEN-010 | Admin date format: tomorrow | A scheduled shot date is tomorrow. | Inspect the admin status/date text. | Date format is `Mañana, {hh:mm}`. |
| TC-GEN-011 | Admin date format: other date | A draft or scheduled shot date is not yesterday, today or tomorrow. | Inspect the admin status/date text. | Date format is `{d month yyyy}, {hh:mm}`. |
| TC-GEN-012 | New frontend icon | Superevento frontend page has share/pagination controls. | Inspect controls that use the curved arrow icon. | `ICON ARROW-CURVED_RIGHT_OUTLINE` is displayed according to the frontend system. |
| TC-GEN-013 | New dark button style | Superevento frontend page has dark button controls. | Inspect normal, hover and disabled button states. | Buttons use the new dark-button style and `color-greyscale-85`. |
| TC-GEN-014 | Theme-specific button radius | Test site uses a theme with a non-default button border radius. | Inspect dark buttons on that theme. | Button radius follows the theme-specific radius while other styling matches the mockup. |
| TC-GEN-015 | New Admin icons | Admin Emision/Tabla pages are available. | Inspect shot, add, discard, expand and cancel controls. | Admin displays `ICON-SHOT`, `ICON-SHOT_ADD`, `ICON-SHOT_DISCARD`, `ICON-ARROW-EXPAND` and `ICON-EDIT-CANCEL` where specified. |

### 1. Shot in Frontend

| ID | Requirement | Preconditions | Steps | Expected result |
| --- | --- | --- | --- | --- |
| TC-FE-001 | Shot minimum content | Superevento has one published shot with text only. | Open frontend superevento page. | Shot is visible with minimum content and no layout break. |
| TC-FE-002 | Shot maximum/multiple content | Superevento has a shot with text, images, video and pivot. | Open frontend superevento page and inspect the shot. | All included content blocks render in the shot; there is no maximum display cap that hides valid content. |
| TC-FE-003 | Shot comments link absent | Superevento has at least one published shot. | Inspect the shot action area. | Comments link is not displayed for the shot. |
| TC-FE-004 | Shot author visible | Superevento has at least one published shot. | Inspect the shot header/metadata. | Current shot author is displayed. |
| TC-FE-005 | Shot avatar metadata | Superevento has at least one published shot with author avatar. | Inspect avatar image, alt text, tooltip and link. | Avatar source is the current author avatar; alt and tooltip equal author name; link points to author page. |
| TC-FE-006 | Author page navigation | Superevento has at least one published shot. | Click the shot author/avatar link. | Browser navigates to the author's page for that site. |
| TC-FE-007 | Content block interaction | Superevento shot includes interactive post-like content blocks. | Interact with links, pivots, video controls or other supported blocks. | Blocks behave the same way as equivalent post-page blocks. |
| TC-FE-008 | Publication datetime visible | Superevento has at least one published shot. | Inspect each visible shot. | Publication/update datetime is visible on the shot. |
| TC-FE-009 | Twitter sharing opens | Superevento has at least one published shot. | Click the share icon, then choose Twitter. | Social-share component becomes visible and Twitter share action is available. |
| TC-FE-010 | Share URL anchors to shot | Superevento has at least one published shot. | Inspect or open the Twitter share URL. | Shared URL includes the current shot anchor and scrolls to the correct shot position. |
| TC-FE-011 | Share close button | Social-share component is open. | Click close. | Social-share is hidden and the original share button is shown again. |
| TC-FE-012 | Share tooltips | Superevento has at least one published shot. | Hover/focus share and close buttons. | Share tooltip is `Compartir`; close tooltip is `Cerrar`. |
| TC-FE-013 | Logged-author edit link | Logged-in author views frontend superevento page containing their shot. | Inspect and click the edit link for that shot. | Link opens Admin edit page for the corresponding shot in a new tab. |
| TC-FE-014 | Superevento shot anchor URL | Superevento has a published shot with id. | Navigate directly to `/evento/{slug}-{id}#s-{idShot}`. | Page loads the superevento and scrolls to the target shot. |
| TC-FE-015 | Redirected superevento shot anchor URL | Superevento has redirected URL variant and a published shot. | Navigate to `/evento/{slug}-{id}-{n}#s-{idShot}`. | Page loads redirected superevento and scrolls to the target shot. |
| TC-FE-016 | Directisimo shot anchor URL | Directisimo has a published shot with id. | Navigate directly to `/live/{slug}-{id}#s-{idShot}`. | Page loads the directisimo and scrolls to the target shot. |
| TC-FE-017 | Redirected directisimo shot anchor URL | Directisimo has redirected URL variant and a published shot. | Navigate to `/live/{slug}-{id}-{n}#s-{idShot}`. | Page loads redirected directisimo and scrolls to the target shot. |
| TC-FE-018 | Deleted shot hidden by default | Superevento has a deleted shot and other visible shots. | Open the superevento page without the deleted shot anchor. | Deleted shot is not shown in the default listing. |
| TC-FE-019 | Deleted shot warning via anchor | Superevento has a deleted shot whose anchor is known. | Open the superevento page with the deleted shot anchor. | Deleted-shot view/warning is displayed to explain that the content no longer exists. |
| TC-FE-020 | Deleted shot warning not shown for valid anchors | Superevento has published and deleted shots. | Open a valid published shot anchor. | Published shot is shown; deleted-shot warning is not shown. |
| TC-FE-021 | Reverse chronological order | Superevento has multiple published shots. | Compare visible shot datetimes from top to bottom. | Shots are ordered most recent first. |
| TC-FE-022 | Divider date for one day | Superevento page contains shots from one publication day. | Inspect shot list. | One divider date appears before the shots for that day. |
| TC-FE-023 | Divider date for several days | Superevento page contains shots from multiple publication days. | Inspect shot list. | Divider date appears before each day group. |
| TC-FE-024 | No empty divider date | Superevento has days without shot publication between active days. | Inspect divider dates. | No divider is shown for days with no shots. |
| TC-FE-025 | Divider repeats across pagination | A day has shots split across frontend pagination. | Navigate between pages where the same day continues. | Divider date appears again on the next page before that day's shots. |
| TC-FE-026 | Divider current-year date | Superevento has current-year shots. | Inspect divider date. | Format is `{weekday}, {d} de {month}`, with `Hoy,` or `Ayer,` prefix when applicable. |
| TC-FE-027 | Divider past-year date | Superevento has prior-year shots. | Inspect divider date. | Format is `{weekday}, {d} de {month} de {yyyy}`, with `Ayer,` prefix when applicable. |
| TC-FE-028 | Pagination appears after 50 shots | Superevento has more than 50 published shots. | Open frontend page. | Shot list is split into pages of 50 and pagination controls are shown. |
| TC-FE-029 | Pagination previous button | Visitor is not on most recent frontend shot page. | Click previous/more recent control. | More recent page is loaded; tooltip is `Más recientes`; control is absent on most recent page. |
| TC-FE-030 | Pagination next button | Visitor is not on oldest frontend shot page. | Click next/older control. | Older page is loaded; tooltip is `Más antiguos`; control is disabled on oldest page. |
| TC-FE-031 | Pagination responsive text | Pagination controls are viewed on mobile and tablet/desktop. | Compare button labels across breakpoints. | Text is hidden on mobile and visible from tablet upwards. |
| TC-FE-032 | Calendar opens | Superevento has shot pagination/calendar available. | Click go-to-date button. | Calendar widget opens; tooltip is `Abrir calendario`. |
| TC-FE-033 | Calendar close | Calendar widget is open. | Click close. | Calendar closes; tooltip is `Cerrar calendario`. |
| TC-FE-034 | Calendar month-year options | Superevento has shots across multiple months and years, with at least one empty month. | Open month/year selector. | Options include only months with publications, grouped by year, ordered most recent first. |
| TC-FE-035 | Calendar default month-year | Superevento has shots across multiple months. | Open calendar. | Default month/year is the most recent month/year with published shots. |
| TC-FE-036 | Calendar previous month | Calendar is not at first available month. | Click previous month. | Table updates to previous month and selector value changes; first month disables the button. |
| TC-FE-037 | Calendar next month | Calendar is not at last available month. | Click next month. | Table updates to next month and selector value changes; last month disables the button. |
| TC-FE-038 | Calendar days without shots | Calendar displays a month with empty days. | Hover/focus a disabled day. | Day is plain text, not clickable, and tooltip is `Sin publicaciones`. |
| TC-FE-039 | Calendar days with shots | Calendar displays a day with shots. | Hover/focus and click the day. | Tooltip is `Ver publicaciones del {d} de {month} de {yyyy}` and link scrolls to the divider date for that day. |
| TC-FE-040 | Calendar first-shot shortcut | Calendar widget is open. | Click first-shot shortcut. | User is sent to the oldest page and scrolled to the oldest shot. |
| TC-FE-041 | Calendar last-shot shortcut | Calendar widget is open. | Click last-shot shortcut. | User is sent to the most recent page and scrolled to the most recent shot. |
| TC-FE-042 | Superevento SEO datemodified | A new shot is published in a superevento. | Inspect structured data/date modified for the superevento. | `dateModified` is updated after the new shot. |
| TC-FE-043 | Directisimo SEO datemodified | A new shot is published in a directisimo. | Inspect structured data/date modified for the directisimo. | `dateModified` is updated after the new shot. |
| TC-FE-044 | JSON-LD authors aggregation | Superevento has shots from multiple authors. | Inspect JSON-LD creators/authors. | All authors who created shots are included as creators of the superevento. |
| TC-FE-045 | Co-authorship unsupported | A shot exists in frontend. | Inspect shot author metadata. | Shot shows a single author only. |
| TC-FE-046 | Affiliation automation support | Shot contains affiliate-supported content. | Open frontend page and inspect affiliate automation output. | Affiliate automations run for shots as they do in supported post contexts. |
| TC-FE-047 | No standalone shot page in superevento | Published shot exists in a superevento. | Attempt to open shot outside the superevento anchor pattern. | Product does not expose a separate single page for the superevento shot. |

### 2. Create a Shot

| ID | Requirement | Preconditions | Steps | Expected result |
| --- | --- | --- | --- | --- |
| TC-CRT-001 | Role access: Director | Director user can access Admin. | Log in as Director and open active superevento Emision tab. | Shot creation form is available. |
| TC-CRT-002 | Role access: Coordinador | Coordinador user can access Admin. | Log in as Coordinador and open active superevento Emision tab. | Shot creation form is available. |
| TC-CRT-003 | Role access: Admin | Admin user can access Admin. | Log in as Admin and open active superevento Emision tab. | Shot creation form is available. |
| TC-CRT-004 | Role access: Editor | Editor user can access Admin. | Log in as Editor and open active superevento Emision tab. | Shot creation form is available. |
| TC-CRT-005 | Role access: SEO | SEO user can access Admin. | Log in as SEO and open active superevento Emision tab. | Shot creation form is available. |
| TC-CRT-006 | Role access: Ecommerce | Ecommerce user can access Admin. | Log in as Ecommerce and open active superevento Emision tab. | Shot creation form is available. |
| TC-CRT-007 | Role access: Colaborador | Colaborador user can access Admin. | Log in as Colaborador and open active superevento Emision tab. | Shot creation form is available. |
| TC-CRT-008 | Emision active state | Superevento is under active follow-up. | Open Emision tab. | Form to create shot is present and existing contents are listed. |
| TC-CRT-009 | Emision over state | Superevento follow-up is over. | Open Emision tab. | Shot form is not present; previously created contents are still displayed. |
| TC-CRT-010 | Superevento title shown | Superevento has title. | Open Emision tab. | Superevento title is displayed. |
| TC-CRT-011 | Empty title fallback | Superevento title is empty. | Open Emision tab. | Title displays `(sin título)`. |
| TC-CRT-012 | Shot tab default | Active superevento has Emision tab. | Open Emision tab. | Shot form tab is selected by default. |
| TC-CRT-013 | Shot tab tooltip | Active superevento has Emision tab. | Hover/focus tab-shot. | Tooltip is `Crear nuevo shot`. |
| TC-CRT-014 | Publish button tooltip | Shot form is visible. | Hover/focus publish button. | Tooltip is `Publicar ya`. |
| TC-CRT-015 | Schedule button tooltip | Shot form is visible. | Hover/focus schedule button. | Tooltip is `Programar`. |
| TC-CRT-016 | Delete button tooltip | Shot form is visible. | Hover/focus delete/discard button. | Tooltip is `Borrar contenido`. |
| TC-CRT-017 | To-draft button tooltip | Shot form is visible. | Hover/focus save-as-draft button. | Tooltip is `Guardar como borrador`. |
| TC-CRT-018 | Preview button tooltip | Shot form is visible. | Hover/focus preview button. | Tooltip is `Vista previa`. |
| TC-CRT-019 | Create text shot | Active superevento exists. | Enter text in shot form and publish/save. | Shot can be created with text block. |
| TC-CRT-020 | Create image shot | Active superevento exists and image fixture is available. | Insert image asset and publish/save. | Shot can be created with image block. |
| TC-CRT-021 | Create video shot | Active superevento exists and video URL is available. | Insert video asset and publish/save. | Shot can be created with video block. |
| TC-CRT-022 | Create GIF shot | Active superevento exists and GIF URL/fixture is available. | Insert GIF asset and publish/save. | Shot can be created with GIF block. |
| TC-CRT-023 | Create table shot | Active superevento exists. | Insert table asset and publish/save. | Shot can be created with table block. |
| TC-CRT-024 | Create graph shot | Active superevento exists and graph URL/fixture is available. | Insert graph asset and publish/save. | Shot can be created with graph block. |
| TC-CRT-025 | Create code shot | Active superevento exists. | Insert code block and publish/save. | Shot can be created with code block. |
| TC-CRT-026 | Create pivot shot | Active superevento exists and pivot target exists. | Insert pivot asset and publish/save. | Shot can be created with pivot block. |
| TC-CRT-027 | Multiple assets in shot | Active superevento exists. | Create one shot with text, image, video and pivot. | Shot saves and renders all selected asset types. |
| TC-CRT-028 | No categories for shot | Shot form is visible. | Inspect form fields. | Category selector is not present or not eligible for shots. |
| TC-CRT-029 | No tags for shot | Shot form is visible. | Inspect form fields. | Tag selector is not present or not eligible for shots. |
| TC-CRT-030 | Create before parent published | Parent superevento is draft/unpublished but active. | Open Emision and create a shot. | Shot can be created before parent superevento is published. |
| TC-CRT-031 | Clear non-empty form | Shot form has unsaved content. | Click delete/discard content button. | Form is cleared and no draft is created. |
| TC-CRT-032 | Clear empty form | Shot form is empty. | Click delete/discard content button. | Nothing changes and no error/draft is created. |
| TC-CRT-033 | Save non-empty draft | Shot form has content. | Click save-as-draft. | Draft render-shot is created, highlighted and expanded; form is cleared and focus remains on form. |
| TC-CRT-034 | Save empty draft | Shot form is empty. | Click save-as-draft. | Empty draft is not saved and user is informed. |
| TC-CRT-035 | Draft snackbar link | A shot has just been saved as draft. | Click snackbar link. | Page scrolls smoothly to the created draft in render groups. |
| TC-CRT-036 | Preview current shot | Shot form has unsaved content. | Click preview. | New tab opens superevento preview at the position where current shot would be placed. |
| TC-CRT-037 | Sticky toolbar hidden near top on mobile | Mobile viewport; form top actions are visible. | Open shot form. | Sticky main-actions toolbar is hidden. |
| TC-CRT-038 | Sticky toolbar visible while form scrolled | Mobile viewport; form remains in view but top actions are off screen. | Scroll within the form. | Sticky main-actions toolbar appears. |
| TC-CRT-039 | Sticky toolbar hidden when form out of view | Mobile viewport. | Scroll until form is no longer visible. | Sticky main-actions toolbar disappears. |
| TC-CRT-040 | No sticky toolbar on desktop | Desktop viewport. | Scroll through shot form. | Sticky main-actions toolbar is not available. |
| TC-CRT-041 | Add asset at cursor | Shot form is focused at a specific cursor position. | Insert an asset from toolbar-add_asset. | Asset is inserted at the current cursor position. |
| TC-CRT-042 | Menu add asset on new line | User adds a new line in shot form. | Open menu-add_asset and insert an asset. | Toolbar opens at current position and asset is inserted there. |
| TC-CRT-043 | Lower form asset gap | Shot form contains assets. | Inspect gaps between assets and lower form area. | Gaps allow adding a line/asset as in post editor. |
| TC-CRT-044 | Help shortcuts visible | Shot form is visible. | Open help-shortcuts. | Keyboard shortcuts information is displayed. |
| TC-CRT-045 | Keyboard shortcuts reuse | Shot form is focused. | Use homepage-shot editor shortcuts for formatting/assets. | Shortcuts behave the same way as homepage shots editor. |
| TC-CRT-046 | Asset modal reuse | Shot form is visible. | Open asset toolbars/modals. | Assets are managed with the same toolbars and modal windows as homepage shots editor. |
| TC-CRT-047 | Drag and drop image upload | Shot form is visible and image fixture is available. | Drag image file into form/upload drop zone. | Image uploads like homepage shots. |
| TC-CRT-048 | Schedule form opens | Shot form has content. | Click schedule. | Scheduling form is displayed. |
| TC-CRT-049 | Schedule cancel | Scheduling form is open. | Click cancel scheduling. | Scheduling form is hidden; tooltip is `Cancelar programación`. |
| TC-CRT-050 | Schedule default time | User clicks schedule. | Inspect scheduling time field. | Time defaults to five minutes after schedule was opened and no time widget is used. |
| TC-CRT-051 | Schedule default date | User clicks schedule. | Inspect scheduling date field. | Date defaults to today and uses a calendar date picker. |
| TC-CRT-052 | Schedule empty time validation | Scheduling form is open. | Clear time and submit/blur. | Field error is `Error: falta la hora`. |
| TC-CRT-053 | Schedule invalid time validation | Scheduling form is open. | Enter invalid time format and submit/blur. | Field error is `Error: hay que usar formato hh:mm`. |
| TC-CRT-054 | Schedule empty date validation | Scheduling form is open. | Clear date and blur. | Field error is `Error: falta la fecha`. |
| TC-CRT-055 | Schedule invalid date validation | Scheduling form is open. | Enter invalid date format and blur. | Field error is `Error: hay que usar formato dd/mm/aaaa`. |
| TC-CRT-056 | Schedule valid shot | Shot form has valid content and valid future date/time. | Submit scheduling form. | Shot is scheduled; result is highlighted and expanded for current author. |
| TC-CRT-057 | Schedule colleague snackbar | Another author schedules a shot while current user is viewing Emision. | Observe current user's page. | Snackbar shows `{author} ha programado para {datetime} Ver vista previa`; render-shot is not expanded. |
| TC-CRT-058 | Publish valid shot | Shot form has valid content. | Click publish. | Shot is published; result is highlighted and expanded for current author. |
| TC-CRT-059 | Publish snackbar link | Shot has just been published. | Click snackbar link. | New tab opens superevento published page at the new shot anchor. |
| TC-CRT-060 | Publish colleague snackbar | Another author publishes a shot while current user is viewing Emision. | Observe current user's page. | Snackbar shows `{author} acaba de publicar. Ver en el superevento`; render-shot is not expanded. |

### 2.6.5-2.6.12 Render Shots, Render Groups, Slots, Search and Filters

| ID | Requirement | Preconditions | Steps | Expected result |
| --- | --- | --- | --- | --- |
| TC-RND-001 | Render draft state | A draft shot exists. | Open Emision render groups. | Draft render-shot is displayed with draft status. |
| TC-RND-002 | Render scheduled state | A scheduled shot exists. | Open Emision render groups. | Scheduled render-shot is displayed with scheduled status. |
| TC-RND-003 | Render published state | A published shot exists. | Open Emision render groups. | Published render-shot is displayed with published status. |
| TC-RND-004 | Render preview state | Any shot exists. | Start preview/send-to-draft confirmation flow. | Render-shot preview version shows full content and only preview action where specified. |
| TC-RND-005 | Render pending changes | Published/scheduled shot has unconsolidated edits. | Open Emision render groups. | Status changes color/text to pending changes. |
| TC-RND-006 | Render highlighted state | A shot was just created or changes consolidated. | Observe render-shot. | Highlighted status appears, then fades after 5 seconds with 2 second fade transition. |
| TC-RND-007 | Render expanded state | Current user creates or updates a shot. | Observe resulting render-shot. | Render-shot is expanded by default with full content and actions. |
| TC-RND-008 | Expanded collapse after new update | A current-user render-shot is expanded. | Create or update another shot. | Previous expanded render-shot collapses. |
| TC-RND-009 | Always-expanded config | Render groups are visible. | Enable `Ver todo desplegado`. | All contents are shown expanded until option is toggled off. |
| TC-RND-010 | Status text draft | Draft shot exists. | Inspect status text. | Status text is `Borrador — Editado: {datetime}` with admin date format. |
| TC-RND-011 | Status text scheduled | Scheduled shot exists. | Inspect status text. | Status text is `Programado — {datetime}` with admin date format. |
| TC-RND-012 | Status text published | Published shot exists. | Inspect status text. | Status text is `Publicado — {datetime}` with admin date format. |
| TC-RND-013 | Status ellipsis | Status text is longer than fixed width. | Inspect status component. | Text truncates with ellipsis. |
| TC-RND-014 | Expand button | Collapsed render-shot exists. | Click expand button. | Corresponding shot content expands and pushes lower shots downward. |
| TC-RND-015 | Content excerpt first text asset | Collapsed shot starts with text over 280 chars. | Inspect collapsed content. | First 280 characters or sufficient text excerpt is visible. |
| TC-RND-016 | Content excerpt short text plus next asset | Collapsed shot starts with short text and a second asset. | Inspect collapsed content. | Short text and enough of second asset are included to fill visible area. |
| TC-RND-017 | Content excerpt non-text first asset | Collapsed shot starts with an image/video/pivot. | Inspect collapsed content. | Only first asset is included in collapsed preview. |
| TC-RND-018 | Collapsed content not actionable | Collapsed shot contains a link. | Click the link area. | Content is not editable/actionable while collapsed. |
| TC-RND-019 | Expanded full content not actionable | Expanded shot contains a link. | Click the link area. | Full content is visible but not editable/actionable in render-shot view. |
| TC-RND-020 | Render author metadata | Shot exists. | Inspect render-shot author. | Author avatar alt text and tooltip equal author name. |
| TC-RND-021 | Render preview button | Shot exists in render groups. | Click preview. | Superevento preview opens in a new tab at shot position. |
| TC-RND-022 | Render edit button | Shot exists in render groups. | Click edit. | Edit form opens for that shot. |
| TC-RND-023 | Render menu button | Shot exists in render groups. | Click more-actions menu. | Menu opens; tooltip is `Más acciones`. |
| TC-RND-024 | Send-to-draft only for published/scheduled | Draft, scheduled and published shots exist. | Open action menu for each. | Send-to-draft is available only for scheduled and published shots. |
| TC-RND-025 | Delete only for draft | Draft, scheduled and published shots exist. | Open action menu for each. | Delete action is available only for draft shots. |
| TC-GRP-001 | Groups in active follow-up | Active superevento has shots. | Open Emision. | Render groups appear below the shot form. |
| TC-GRP-002 | Groups when follow-up over | Superevento follow-up is over and has shots. | Open Emision. | Render groups appear below status message; shot form is absent. |
| TC-GRP-003 | Published group defaults | Published shots exist. | Open Emision. | Published group is uncollapsed by default. |
| TC-GRP-004 | Published group ordering | Multiple published shots exist. | Inspect published group. | Shots are reverse chronological by published date and grouped by day. |
| TC-GRP-005 | Published group pagination preserves page | Published group spans pages. | Navigate published pagination. | Other groups and shot form remain visible while published group page changes. |
| TC-GRP-006 | Scheduled group defaults | Scheduled shots exist. | Open Emision. | Scheduled group is uncollapsed by default. |
| TC-GRP-007 | Scheduled group ordering | Multiple scheduled shots exist. | Inspect scheduled group. | Shots are reverse chronological by scheduled date. |
| TC-GRP-008 | Draft group defaults | Draft shots exist. | Open Emision. | Draft group is collapsed by default. |
| TC-GRP-009 | Draft group ordering | Multiple drafts exist. | Expand draft group. | Drafts are reverse chronological by update date. |
| TC-GRP-010 | Group header count | Render groups contain shots. | Inspect group headers. | Header displays group name and count, unless count was intentionally removed for performance. |
| TC-GRP-011 | Header toggles group | Render group is visible. | Click group header. | Group collapses/uncollapses and icon changes accordingly. |
| TC-GRP-012 | Toggle tooltip | Render group is visible. | Hover/focus toggle. | Tooltip is `Plegar` when open and `Desplegar` when closed. |
| TC-GRP-013 | Group preference persists | User toggles group states. | Leave page and return. | Each group returns in the state last left by user. |
| TC-GRP-014 | Published go-to-date availability | Published, scheduled and draft groups exist. | Inspect group controls. | Go-to-date appears only in published group. |
| TC-GRP-015 | Published group calendar active days | Published group calendar is open. | Inspect days and click active day. | Only days with published shots are clickable and jump to relevant render-shots. |
| TC-GRP-016 | Published group prev pagination | Published group is not on first page. | Click previous. | Previous page opens; button is disabled on first page. |
| TC-GRP-017 | Published group next pagination | Published group is not on last page. | Click next. | Next page opens; button is disabled on last page. |
| TC-GRP-018 | Published group divider date | Published shots span multiple days. | Inspect published group. | Divider date appears per day using frontend divider-date rules. |
| TC-GRP-019 | Config menu opens | Render groups visible. | Click config button. | Menu opens with config options. |
| TC-GRP-020 | Config expanded option | Config menu open. | Toggle `Ver todo desplegado`. | All content is expanded by default; toggling again restores default. |
| TC-GRP-021 | Config current author option | Config menu open. | Toggle `Ver sólo tus contenidos`. | Groups show only current user's content and headers show current-user counts. |
| TC-GRP-022 | Config does not force expansion | Some groups are collapsed. | Apply expanded/current-author config. | Current collapsed/uncollapsed group state is preserved. |
| TC-GRP-023 | Filter icon in group header | Current-author filter is active. | Inspect render-group headers. | Filter icon appears in headers to communicate active filter. |
| TC-GRP-024 | Floating new button hidden near form | Shot form visible on screen. | Inspect page. | Floating new button is not visible. |
| TC-GRP-025 | Floating new button visible after scroll | Shot form is off screen and add/paste FABs are not visible. | Scroll down render groups. | Floating new button appears. |
| TC-GRP-026 | Floating new button action | Floating new button visible. | Click it. | Same behavior as save-as-draft/new flow; focus returns to empty shot form. |
| TC-SLT-001 | Slots table opens | Scheduling form is open. | Click slot button. | Superevento slots table opens; tooltip is `Elegir hueco`. |
| TC-SLT-002 | Slots today button | Slots table is open on another day/week. | Click today. | Table returns to today; tooltip is `Ir a hoy`. |
| TC-SLT-003 | Slots go-to-date | Slots table is open. | Click go-to-date and select day. | Selected day becomes first day position; tooltip is `Ir al día...`. |
| TC-SLT-004 | Slots previous week | Slots table is open. | Click previous week. | Previous week is shown; tooltip is `Ir a la semana anterior`. |
| TC-SLT-005 | Slots next week | Slots table is open. | Click next week. | Next week is shown; tooltip is `Ir a la semana siguiente`. |
| TC-SLT-006 | Slots refresh | Slots table is open. | Click refresh after another schedule is created. | Results refresh; tooltip is `Refrescar`. |
| TC-SLT-007 | Busy slot with one shot | One shot is scheduled for a slot. | Inspect the busy slot cell. | Cell shows publish time in `hh:mm` and the shot excerpt. |
| TC-SLT-008 | Busy slot with multiple shots | Multiple shots are scheduled for a slot. | Inspect the busy slot cell. | Cell shows `{count} contenidos`. |
| TC-SLT-009 | Busy slot hover overlay | Busy slot contains scheduled shots. | Hover the slot. | Overlay lists each scheduled content with `hh:mm` time and excerpt, fixed width and ellipsis overflow. |
| TC-SRC-001 | Emision search result header | Render groups contain matching and nonmatching shots. | Search by a shot text query. | Header shows total result count and a dismiss link. |
| TC-SRC-002 | Emision search expands groups | Some render groups are collapsed. | Perform search. | All render groups become visible/uncollapsed automatically. |
| TC-SRC-003 | Emision search filters groups | Search query matches content in multiple statuses. | Perform search. | Each render group shows only matching render-shots and its matching count. |
| TC-SRC-004 | Emision search dismiss by input x | Search is active. | Click x in search form. | Search is dismissed and original listings return. |
| TC-SRC-005 | Emision search dismiss by header link | Search is active. | Click link under search header. | Search is dismissed and original listings return. |
| TC-FLT-001 | Current author filter applies | Render groups include current-user and other-user shots. | Enable `Ver sólo tus contenidos`. | Only current-user shots are shown; counts reflect current-user results. |
| TC-FLT-002 | Current author filter preserves group state | Some groups are collapsed before filter. | Enable current-author filter. | Groups remain collapsed/uncollapsed as before. |
| TC-FLT-003 | Current author filter dismiss | Current-author filter is active. | Toggle same option again. | Original unfiltered listings return. |

### 3. Edit a Shot

| ID | Requirement | Preconditions | Steps | Expected result |
| --- | --- | --- | --- | --- |
| TC-EDT-001 | Edit content assets | Shot exists with editable content. | Open edit form and modify text/image/video/GIF/table/graph/code/pivot blocks. | Content edits work the same way as create flow and post editor asset edits. |
| TC-EDT-002 | Any editor can edit | Shot was created by another user. | Log in as a different allowed role and click edit. | Edit form opens unless another user is actively editing the shot. |
| TC-EDT-003 | Published to draft prompt opens | Published shot exists. | Choose send/pass to draft. | Confirmation prompt opens with preview render. |
| TC-EDT-004 | Published to draft cancel button | Prompt is open for published shot. | Click `Cancelar`. | Prompt closes and shot remains published. |
| TC-EDT-005 | Published to draft Esc | Prompt is open for published shot. | Press Esc. | Prompt closes and shot remains published. |
| TC-EDT-006 | Published to draft overlay click | Prompt is open for published shot. | Click overlay. | Prompt closes and shot remains published. |
| TC-EDT-007 | Published to draft confirm | Prompt is open for published shot. | Click `A borrador`. | Shot converts to draft; scroll remains in place; snackbar links to new draft in expanded view. |
| TC-EDT-008 | Scheduled to draft prompt opens | Scheduled shot exists. | Choose send/pass to draft. | Confirmation prompt opens with preview render. |
| TC-EDT-009 | Scheduled to draft confirm | Prompt is open for scheduled shot. | Click `A borrador`. | Shot converts to draft; snackbar links to new draft in expanded view. |
| TC-EDT-010 | Edit form inline location | Shot exists in render group. | Click edit. | Edit form appears where the render-shot is located. |
| TC-EDT-011 | Single edit form rule | One shot edit form is open. | Click edit on another shot. | First edit form closes automatically and second edit form opens. |
| TC-EDT-012 | Autosave on cancel edit | Published or scheduled shot edit form is open with changes. | Click cancel edit. | Form closes; changes remain autosaved and pending. |
| TC-EDT-013 | Autosave on switching edit target | Shot edit form has unsaved changes. | Click edit on another shot. | First shot changes remain autosaved and pending. |
| TC-EDT-014 | Autosave on browser close/reopen | Shot edit form has unsaved changes. | Close tab/browser, reopen Admin and inspect shot. | Changes remain in the shot and are pending when applicable. |
| TC-EDT-015 | Scroll after edit action | Edit form is closed through an action. | Complete/cancel edit. | Scroll is positioned where the updated render-shot is located. |
| TC-EDT-016 | Update draft | Draft shot exists. | Edit draft content and close form. | Draft remains draft, update date changes, it becomes most recent draft, and render-shot is highlighted. |
| TC-EDT-017 | Draft has no pending changes state | Draft shot is edited. | Inspect status after edit. | Draft is not marked pending changes. |
| TC-EDT-018 | Update published without republish | Published shot exists. | Edit content with republish checkbox unchecked and close/consolidate as specified. | Shot remains published with same publication date and same render-group position. |
| TC-EDT-019 | Update scheduled | Scheduled shot exists. | Edit content and optionally date. | Shot remains scheduled and relocates according to scheduled date if it changed. |
| TC-EDT-020 | Cancel edit tooltip | Edit form is open. | Hover/focus cancel edit button. | Tooltip is `Cancelar edición`. |
| TC-EDT-021 | Edit to draft button tooltip | Editing published or scheduled shot. | Hover/focus pass-to-draft button. | Tooltip is `Pasar a borrador`; button is only available for published/scheduled shots. |
| TC-EDT-022 | Republish checkbox availability | Editing a published shot. | Inspect republish checkbox. | Checkbox is available only for published shots and unchecked by default. |
| TC-EDT-023 | Republish checkbox unchecked | Editing a published shot. | Leave republish unchecked and save/consolidate changes. | Current publication date is kept. |
| TC-EDT-024 | Republish checkbox checked | Active follow-up; editing a published shot. | Check republish and complete action. | Publication date updates to current moment and shot moves to new position in published group. |
| TC-EDT-025 | Republish scroll | Republish action completed. | Observe page after completion. | User scrolls smoothly to the shot's new position. |
| TC-EDT-026 | Republish disabled when over | Follow-up is over; editing published shot. | Inspect republish checkbox. | Checkbox is disabled with tooltip `No se puede republicar si el seguimiento ha terminado`. |
| TC-EDT-027 | Schedule changes button availability | Editing scheduled shot. | Inspect action buttons. | Schedule-changes button is available only for scheduled shots and appears next to slots button when screen is wide enough. |
| TC-EDT-028 | Pending changes published | Published shot edit form has changes, then user exits without publishing them. | Inspect render-shot. | Shot remains published and is marked pending changes. |
| TC-EDT-029 | Pending changes scheduled | Scheduled shot edit form has changes, then user exits without scheduling them. | Inspect render-shot. | Shot remains scheduled and is marked pending changes. |
| TC-EDT-030 | Pending changes snackbar published | Published shot has pending changes. | Edit that shot again. | Snackbar warns about pending changes and does not include a publish shortcut. |
| TC-EDT-031 | Pending changes snackbar scheduled | Scheduled shot has pending changes. | Edit that shot again. | Snackbar warns about pending changes and does not include a schedule shortcut. |
| TC-EDT-032 | Edit taken blocked | Another editor is actively editing a shot. | Current user clicks edit for that shot. | Edit is blocked and snackbar states `{editor} está editando este contenido ahora mismo, puedes intentarlo más tarde`. |
| TC-EDT-033 | Edit taken snackbar close button | Edit-taken snackbar is visible. | Click close. | Snackbar closes. |
| TC-EDT-034 | Edit taken snackbar Esc | Edit-taken snackbar is visible. | Press Esc. | Snackbar closes. |
| TC-EDT-035 | Edit taken snackbar overlay click | Edit-taken snackbar is visible. | Click overlay. | Snackbar closes. |

### 4. Listing Shots in Tabla Tab

| ID | Requirement | Preconditions | Steps | Expected result |
| --- | --- | --- | --- | --- |
| TC-TAB-001 | Tabla tab available | Superevento has shot contents. | Open Admin superevento and switch to Tabla tab. | Tabla tab loads table listings for shot content. |
| TC-TAB-002 | Tabla title | Superevento has title. | Open Tabla tab. | Superevento title is displayed. |
| TC-TAB-003 | Publicaciones tab tooltip | Tabla tab is visible. | Hover/focus Publicaciones subtab. | Tooltip is `Ver programados y publicados`. |
| TC-TAB-004 | Borradores tab tooltip | Tabla tab is visible. | Hover/focus Borradores subtab. | Tooltip is `Ver borradores`. |
| TC-TAB-005 | Publicaciones includes scheduled | Scheduled shot exists. | Open Tabla > Publicaciones. | Scheduled shot is listed. |
| TC-TAB-006 | Publicaciones includes published | Published shot exists. | Open Tabla > Publicaciones. | Published shot is listed. |
| TC-TAB-007 | Publicaciones state scheduled | Scheduled shot row is listed. | Inspect `td-state`. | Clock button is displayed with tooltip `Editar programación`. |
| TC-TAB-008 | Inline schedule edit | Scheduled shot row is listed. | Click clock button. | Inline schedule edit form opens using the same behavior as Dashboard posts. |
| TC-TAB-009 | Publicaciones state published | Published shot row is listed. | Inspect `td-state`. | Check icon is displayed with tooltip `Publicado`. |
| TC-TAB-010 | Publicaciones datetime | Scheduled/published row is listed. | Inspect `td-datetime`. | Date/time reflects scheduled/published date and uses admin format with ` — ` separator. |
| TC-TAB-011 | Publicaciones author | Scheduled/published row is listed. | Inspect `td-author`. | Author of the shot is displayed and overflows with ellipsis if needed. |
| TC-TAB-012 | Publicaciones type icon | Scheduled/published row is listed. | Inspect `td-type`. | Shot icon represents row content type. |
| TC-TAB-013 | Publicaciones content column unused | Scheduled/published row is listed. | Inspect `td-content`. | Column is not used for shot content type. |
| TC-TAB-014 | Publicaciones text excerpt | Scheduled/published row is listed. | Inspect `td-text`. | Shot excerpt follows homepage-shot excerpt rules and overflows with ellipsis. |
| TC-TAB-015 | Publicaciones preview action | Scheduled/published row is listed. | Click preview. | Current shot preview opens in a new tab at shot position; tooltip is `Vista previa`. |
| TC-TAB-016 | Publicaciones edit action | Scheduled/published row is listed. | Click edit. | UI switches to Emision tab and opens edit form for the shot; tooltip is `Editar`. |
| TC-TAB-017 | Publicaciones pass to draft action | Scheduled/published row is listed. | Click pass-to-draft. | Send-to-draft confirmation flow starts; tooltip is `Pasar a borrador`. |
| TC-TAB-018 | Publicaciones default ordering | Multiple scheduled/published rows exist. | Open Publicaciones. | Rows are ordered by latest `td-datetime` first. |
| TC-TAB-019 | Publicaciones ordering toggle | Multiple scheduled/published rows exist. | Toggle `td-datetime` sort. | Ordering switches between latest first and earliest first. |
| TC-TAB-020 | Publicaciones status filter | Publicaciones has scheduled and published rows. | Filter by `Programados`, then `Publicados`. | Results show only selected status and dropdown options include status icons. |
| TC-TAB-021 | Publicaciones datetime filter | Publicaciones has rows across date range. | Apply datetime range filter. | Results are limited to selected date/time range; default widget range starts at superevent start and ends at last content date/time. |
| TC-TAB-022 | Publicaciones author filter | Publicaciones has rows from multiple authors. | Select one or more authors. | Results show only selected authors. |
| TC-TAB-023 | Publicaciones type filter | Publicaciones has content type options. | Open type filter. | `Shot` option is available with icon; `RT` and `Directísimo` options may be blank until future tasks define them. |
| TC-TAB-024 | Publicaciones text search filter | Publicaciones has rows with distinct excerpts. | Search within `td-text`. | Results show only rows whose shot excerpt matches query. |
| TC-TAB-025 | Publicaciones column width/overflow | Publicaciones table is visible on desktop and mobile. | Inspect columns. | `td-text` is flexible; other columns are fixed; author/text overflow with ellipsis; no other columns wrap. |
| TC-TAB-026 | Send to draft prompt text | Scheduled/published row is listed. | Start pass-to-draft from Tabla. | Prompt asks `¿Seguro que quieres pasar a borrador {td-text}?`. |
| TC-TAB-027 | Send to draft prompt cancel | Pass-to-draft prompt is open. | Click Cancel, press Esc, or click overlay. | Prompt closes and row remains in Publicaciones. |
| TC-TAB-028 | Send to draft prompt confirm | Pass-to-draft prompt is open. | Click `A borrador`. | Shot converts to draft; success snackbar appears. |
| TC-TAB-029 | Borradores list drafts | Draft shot exists. | Open Tabla > Borradores. | Draft shot is listed. |
| TC-TAB-030 | Borradores datetime | Draft row is listed. | Inspect `td-datetime`. | Date/time is last edited date using the same format as Publicaciones column. |
| TC-TAB-031 | Borradores common columns | Draft row is listed. | Inspect author/type/content/text columns. | Columns follow equivalent data, ordering/filtering, width and overflow rules from Publicaciones. |
| TC-TAB-032 | Borradores preview action | Draft row is listed. | Click preview. | Current draft preview opens in new tab at shot position. |
| TC-TAB-033 | Borradores edit action | Draft row is listed. | Click edit. | UI switches to Emision tab and opens edit form for the draft. |
| TC-TAB-034 | Borradores delete action | Draft row is listed. | Click delete. | Delete confirmation flow starts; tooltip is `Eliminar`. |
| TC-TAB-035 | Tabla search both subtabs | Publicaciones and Borradores have searchable rows. | Search from Tabla appbar/search form. | Search runs across both subtabs and total result number includes both. |
| TC-TAB-036 | Tabla search tab counts | Search is active. | Inspect Publicaciones and Borradores subtabs. | Each subtab reflects its corresponding result count. |
| TC-TAB-037 | Tabla search results display | Search is active. | Open each subtab. | Each table shows only matching results for that subtab. |
| TC-TAB-038 | Tabla search dismiss | Search is active. | Click header-search nav/dismiss link. | Search is dismissed in both subtabs and all content is shown again. |
| TC-TAB-039 | Tabla filters independent | Tabla has both subtabs with filterable rows. | Apply a filter in Publicaciones, then inspect Borradores. | Filtering is associated to each table independently and does not alter the other subtab unexpectedly. |

### 5. Delete a Shot

| ID | Requirement | Preconditions | Steps | Expected result |
| --- | --- | --- | --- | --- |
| TC-DEL-001 | Delete from Tabla prompt text | Draft shot exists in Tabla > Borradores. | Click delete. | Prompt asks `¿Seguro que quieres eliminar {td-text}? No se podrá deshacer después`. |
| TC-DEL-002 | Delete from Tabla cancel | Delete prompt from Tabla is open. | Click Cancel, press Esc, or click overlay. | Prompt closes and draft remains listed. |
| TC-DEL-003 | Delete from Tabla confirm | Delete prompt from Tabla is open. | Click `Eliminar`. | Draft shot is deleted and success snackbar appears. |
| TC-DEL-004 | Delete from Emision prompt text | Draft shot exists in Emision draft render group. | Open actions and click delete. | Prompt asks `¿Seguro que quieres eliminar? No se podrá deshacer después`. |
| TC-DEL-005 | Delete from Emision cancel | Delete prompt from Emision is open. | Click Cancel, press Esc, or click overlay. | Prompt closes and draft remains listed. |
| TC-DEL-006 | Delete from Emision confirm | Delete prompt from Emision is open. | Click `Eliminar`. | Draft shot is deleted and success snackbar appears. |
| TC-DEL-007 | Deleted linked shot frontend warning | A shot that was linked externally is deleted. | Navigate to the superevento URL with the deleted shot anchor. | Visitor lands on associated superevento main page and sees warning that the content does not exist anymore. |
| TC-DEL-008 | Deleted linked shot SEO behavior | A deleted shot anchor is opened from an external link. | Inspect page and structured data behavior. | Visitor remains on associated superevento page; deleted shot does not expose a standalone page. |
| TC-DEL-009 | Deleted shot absent from normal listings | Deleted shot exists. | Open frontend superevento and Admin render/list views without direct deleted anchor. | Deleted shot is not shown as normal content. |

## Regression Coverage Checklist

- Verify all use cases on Xataka theme.
- Verify all use cases on Default theme.
- Verify frontend responsive mockup breakpoints: mobile, tablet, 800px, 1023px, desktop, 1150px and desktop 1200+.
- Verify Admin responsive mockup breakpoints: mobile and desktop.
- Verify supported roles independently for Create, Edit, Listing and Delete.
- Verify no category or tag controls are eligible for Shot content.
- Verify unsupported sites/themes remain unaffected by the MVP rollout scope.

