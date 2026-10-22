# Lesson 5. Test design, API, and execution

This is the week-4 to week-6 material. Do it after you can scan, steer, buffer, and reuse without looking at the cheat sheet.

## TestCaseDesign

A TestSheet is a table. Rows are instances. Columns are attributes. A template TestCase is linked to the sheet, and Tosca creates one run per instance.

Attributes for a plan picker:

| Attribute | Values |
|---|---|
| Plan | Monthly, Annual with ads, Annual no ads |
| Listed price | 2 per month, 15 per year, 30 per year |
| Trial copy | 1 day free, 1 day free, 1 day free |

One template TestCase:

1. Library block: open and sign in.
2. Select the card whose plan name is the sheet attribute `Plan`.
3. Verify the price against the sheet attribute `Listed price`.
4. Verify the trial line against the sheet attribute `Trial copy`.

On the template, those TestStepValues are linked to the attributes. Instantiating the template writes each row's values into its own TestCase. You do not type a made-up token such as a column function. Drag the attribute onto the value, the way Academy shows it.

Three instances, one template. You do not copy the TestCase three times.

Two combination ideas, and the difference:

- **Linear expansion.** Each row is a case you wrote. Use this when the rows are the real scenarios.
- **All combinations.** Tosca crosses the attributes. Use this only when every crossing is a valid test. Crossing "plan" with an unrelated "browser error" blows up into cases nobody asked for.

Classes group attributes that move together. Price belongs with Plan. Do not make them independent combinations.

## API next to UI

AS2 adds API Scan. Scan the request once. That scan is a Module, the same idea as XScan, for a message instead of a screen.

Use an API TestCase for the rule, and a UI TestCase for the screen:

- API: creating a subscription with a known plan returns the expected status and price.
- UI: the picker shows that plan and that price, and the button label matches the state (start trial versus subscribe).

Do not drive the API check through the browser. Do not assert HTML inside the API TestCase.

## ExecutionLists

An ExecutionList is the suite. Build one list per purpose:

- `Smoke`. Library login plus one plan.
- `Plan picker`. The template, so all instances run.
- `Checkout sandbox`. Only when the environment is a known test mode. Never point this list at production.

The list holds the TCPs for Browser and BaseUrl. Results land on the list: passed, failed, and the step that failed. A failure names the TestStep and the control. The fix is either the Module (identification) or the TestCase (wrong value or wrong ActionMode). Say which before you edit.

## CI, at sketch level

Commander does not have to be clicked for a repeat run. Tosca CI and Distributed Execution can run an ExecutionList from a build. The sketch you should be able to write before any pipeline exists:

1. Which ExecutionList.
2. Which TCPs the build injects (BaseUrl, user).
3. Where the password comes from (the secret store, not the repo).
4. What "failed" means for the build (any failed TestCase fails the job).

## Done when

You can show one template fed by a sheet, one API TestCase for the same rule, and one ExecutionList that would run them. Lab 2 is that exercise.

Next: [Lab 2](../lab/02-exercise-plan-selection.md).
