# Cheat sheet

One page. The lessons have the reasoning.

## Sections

Modules, TestCases, Execution, then TestCaseDesign. Requirements after those feel normal.

## ActionModes

| Mode | Does |
|---|---|
| Input | Type, click, set |
| Verify | Assert the current value |
| WaitOn | Wait until the value matches |
| Buffer | Store the current value |
| Select | Pick a list entry |
| Constraint | Identify a row or card |
| DoNothing | Leave the scanned control alone |

## Values in braces

| Written | Means |
|---|---|
| `{CLICK}` | Click |
| `{DOUBLECLICK}` | Double-click |
| `{ENTER}` | Enter key |
| `{B[Name]}` | Buffer Name |
| `{CP[Name]}` | Test configuration parameter Name |

A value with no braces is literal text.

## Where a value belongs

| The value is | Put it in |
|---|---|
| A locator or identify-by property | Module |
| A business constant for one scenario | TestSheet instance, or the TestStep if it is truly one-off |
| Produced by the system during the run | Buffer |
| URL, browser, user, environment | TCP on the ExecutionList |
| A password or key | CI secret or local TCP, never the TestCase body |
| Steps repeated by many TestCases | Reusable TestStepBlock in a library |

## Identification

Prefer test id, then id or name, then a stable label, then an anchor. Do not identify by index.

## Failure check

1. The control was not found. Fix the Module.
2. The control was found and the value was wrong. Fix the TestCase, the sheet, or the TCP.
3. The control was found too late. Add WaitOn. Do not turn Verify into a wait.

## Course

[Tosca Fundamentals (AS1)](https://academy.tricentis.com/tricentis-tosca-fundamentals-automating-web-application-testing), exercises 01–11, then Automation Specialist 2.
