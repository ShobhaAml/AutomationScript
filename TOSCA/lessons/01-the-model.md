# Lesson 1. The model

A Selenium suite mixes locators and scenario steps in code. Tosca splits them.

| Piece | Holds | Changes when |
|---|---|---|
| Module | The controls on a screen, and how to find them | The UI changes |
| TestCase | The business steps and the data for one scenario | The scenario or the data changes |
| ExecutionList | Which TestCases run, in which configuration | The pipeline or the cycle changes |

A TestStep inside a TestCase points at a Module. It does not contain the locator. Fifty TestCases can use one Login module. When the login button's id changes, you update that one Module.

## The four places you will click

In Tosca Commander the sections that matter first are:

- **Modules.** Output of XScan. Technical.
- **TestCases.** Business flow. This is where ActionModes are set.
- **Execution.** ExecutionLists and results.
- **TestCaseDesign.** Later. Attributes, instances, classes, and the combinations that feed a template.

Requirements come after those four are familiar. Linking a requirement to a TestCase is traceability you already know. It is not the automation.

## What a step looks like

A TestStep is a Module dropped onto a TestCase. Each control you steer becomes a TestStepValue with three fields you fill:

| Control | ActionMode | Value |
|---|---|---|
| Username | Input | `qa.user` |
| Password | Input | `{CP[Password]}` |
| Sign in | Input | `{CLICK}` |
| Welcome | Verify | `Welcome` |

`Input` types or clicks. `Verify` asserts. `{CLICK}` is a click, not the text "CLICK". `{CP[Password]}` reads a test configuration parameter, so the password is not stored on the step.

## Done when

You can take any UI test you have written and sort each line into Module (how to find it) or TestCase (what to do, with which data). If a line is both, it is a locator hiding inside the scenario. That locator belongs on the Module.

Next: [Lab 1](../lab/01-worked-login.md).
