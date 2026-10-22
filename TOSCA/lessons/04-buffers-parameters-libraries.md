# Lesson 4. Buffers, parameters, and libraries

Three reuse tools. They are not interchangeable.

| Tool | Use it for | Syntax |
|---|---|---|
| Buffer | A value the application created during the run | `{B[OrderId]}` |
| Test configuration parameter | A value the run is configured with: URL, user, browser | `{CP[BaseUrl]}` |
| Library / Reusable TestStepBlock | Steps that many TestCases perform the same way | Drag the block in |

## Buffers

A buffer is a named string Tosca remembers for the rest of the execution.

Example: the confirmation page shows an order number. You did not know it before the run.

| Control | ActionMode | Value |
|---|---|---|
| Order number | Buffer | `OrderId` |
| Order number | Verify | `{B[OrderId]}` |

The Buffer row stores the actual text into `OrderId`. A later TestCase, or a later step, reads it back with `{B[OrderId]}`. Use buffers for ids, totals, and dates the system generates. Do not use a buffer for the username. That is configuration.

## Test configuration parameters

A TCP is set on a folder or on the ExecutionList. Every TestCase underneath can read it. Typical names:

| Parameter | Example meaning |
|---|---|
| Browser | Chrome |
| BaseUrl | The environment home page |
| User | The QA user for this list |
| Password | Supplied at runtime, not written into the TestCase |

Predefined parameters also exist. `Browser` is one Academy exercise 09 asks you to set. Use the product's predefined name when Tosca already has one, so the engine picks it up without a custom reader.

Set passwords on the ExecutionList you run locally, or inject them from the CI secret store. Do not check a password into the TestCase.

## Libraries

A Reusable TestStepBlock is a TestCase fragment stored so others can drag it.

Put these in a library:

- Open the application at `{CP[BaseUrl]}`.
- Sign in with `{CP[User]}` and `{CP[Password]}`.
- Close the browser.

Leave these in the business TestCase:

- Choose the plan.
- Assert the price and the trial message.
- Assert the confirmation number.

If a block contains an assertion that only one scenario cares about, it is not a library block. "Login succeeded" can be a block. "The annual plan shows 15 per year" belongs to the scenario.

## Recovery, briefly

A Recovery scenario is a cleanup attached to a TestCase or an ExecutionList: if an unexpected dialog appears, click its button, then retry or continue. Write Recovery for dialogs that are not part of the scenario. Do not hide a product bug by recovering from the assertion you meant to check.

## Done when

You can point at any value in a TestCase and say which of the three tools it belongs to, in one sentence.

Next: [Lesson 5](05-test-design-api-execution.md).
