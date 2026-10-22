# Lesson 3. TestCases and ActionModes

A TestCase is a folder of TestSteps. Each TestStep is one Module. The ActionMode on each TestStepValue says what Tosca does with that control.

## ActionModes you need in week 1 and 2

| ActionMode | Meaning | Typical value |
|---|---|---|
| Input | Type, click, or set | `qa.user`, `{CLICK}`, `{ENTER}` |
| Verify | Actual value must match | `Welcome` |
| WaitOn | Wait until the property matches, then continue | A heading that appears after login |
| Buffer | Save the actual value under a name | Buffer name in the value column |
| Select | Choose an entry in a list or dropdown | The visible option text |
| Constraint | Narrow which row or card, do not steer it | The plan name |

`DoNothing` is the default. A control you scanned but do not use on this step stays on DoNothing. Do not delete it from the Module just because one TestCase ignores it.

Verify compares. It does not wait. If the element appears late, put WaitOn on it, then Verify. Waiting and asserting are separate on purpose.

## Clicks and keys

These values are instructions, written in braces:

| Value | Effect |
|---|---|
| `{CLICK}` | Click the control |
| `{DOUBLECLICK}` | Double-click |
| `{ENTER}` | Send Enter |
| `{B[OrderId]}` | Read the buffer named OrderId |
| `{CP[Browser]}` | Read the test configuration parameter Browser |

A plain value without braces is data. `Welcome` means the text Welcome. `{CLICK}` means a click.

## Shape of a TestCase

Keep the TestCase in business order. Folders inside the TestCase are free and should read like a script a reviewer can skim.

```
TestCase: Valid user sees the plan picker
  Folder: Open
    TestStep: Open URL          url = {CP[BaseUrl]}
  Folder: Sign in
    TestStep: Login
      Username   Input    {CP[User]}
      Password   Input    {CP[Password]}
      Sign in    Input    {CLICK}
  Folder: Check
    TestStep: Plan picker
      Heading    WaitOn   Choose a plan
      Heading    Verify   Choose a plan
```

Pick one assertion that proves the login worked. In the valid-user case that is the Home heading, with WaitOn and then Verify. Do not assert every label on the page.

## Data does not live on the step

A step that says `Password = Summer2024!` cannot be reused and should not be committed. The step says `Password = {CP[Password]}`. The ExecutionList, or a configuration folder, holds the parameter. The lesson 4 file shows where the value is set.

## Done when

You can write three TestCases against the same Login Module (valid user, unknown user, blank password) and the only differences are ActionModes and values. The Module is unchanged.

Next: [Lesson 4](04-buffers-parameters-libraries.md).
