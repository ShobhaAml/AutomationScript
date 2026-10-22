# Lab 2. Plan selection

Do this after lessons 1 through 5. Write your answer under each heading in this file, or in a sibling note. Use a fictional shop. Do not use a production URL, a real password, or a card number.

Business rule to model:

- Three plans are on one page: Monthly, Annual with ads, Annual no ads.
- Each card shows one price.
- A trial line is visible for a plan that still offers a trial.
- A customer who already used a trial sees a pay-today total and no trial line. That is a second scenario, not a third copy of the same TestCase.

## 1. Modules

List each Module, its controls, and the identify-by property or anchor. State one control you will not scan.

## 2. Library blocks

Which steps are Reusable TestStepBlocks? Which steps stay in the business TestCase? One sentence each.

## 3. TestSheet

Draw the sheet. Columns are attributes. At least three instances for the trial scenario. Mark which attributes are a class (they move together) and which combination method you would use. Linear expansion is the right default here. Say why all-combinations would be wrong.

## 4. Template TestCase

One table:

| Folder | Module or block | Control | ActionMode | Value |
|---|---|---|---|---|

The price and the trial line must come from the sheet, not from typed literals. In the Value column write `sheet: Listed price` or `sheet: Trial copy`. In Commander you link the attribute by dragging it onto that TestStepValue. There is no extra function to memorize for that link.

## 5. Second scenario

The customer is no longer eligible for a trial. Write only the rows that differ from the template. Say whether this is a new instance, a new template, or a TCP. Pick one and justify it.

## 6. ExecutionLists

Name two lists. For each, list the TCPs and whether a failed TestCase fails the build.

## 7. Review questions

Answer in a sentence each.

1. The price label's id changes. What do you edit, and what do you leave alone?
2. The order number is generated on the confirmation page. Buffer, TCP, or literal?
3. The run must target a staging host this week and a different host next week. Where does the host go?
4. Verify failed because the heading was not in the DOM yet. Which ActionMode was missing?

When section 7 is easy, you are ready for Academy exercises beyond the first run. Track that in [progress](progress.md).
