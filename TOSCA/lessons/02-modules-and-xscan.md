# Lesson 2. Modules and XScan

XScan is the scanner. You point it at a browser, tick the controls this Module will steer, and Tosca writes the identification. You then delete noise and tighten what is left.

## Scan less than you see

Scan the controls this Module acts on. A login Module needs the two fields, the button, and the error or welcome message you will verify. Leave the header, footer, and unrelated links out. Extra controls become candidates for a bad match later.

## How a control is found

Each ModuleAttribute has technical properties. Tosca decides a match from the properties marked **Identify by**. Prefer a stable property, in this order when the application gives you one:

1. A dedicated test id or automation id.
2. `id` or `name` that the team treats as stable.
3. A visible name or label, if it is unique and not translated every week.
4. An **anchor**: find a nearby stable control, then take the field next to it.

Avoid **index**. Index is "the third input on the page." It breaks when marketing adds a field above it.

## Anchors

Use an anchor when the control itself has no stable id. Example: a price field with no id, sitting next to the label "Total per year". The label is the anchor. The field is identified relative to that label.

In XScan this is "Identify by anchor", not a hand-written XPath you maintain. You can still see the properties. You should still be able to explain which property makes the control unique.

## One Module per screen region, not per test

Good first cut:

- `Open browser` is a Standard Module Tosca already ships. You do not scan it.
- `Login` is your Module.
- `Plan picker` is your Module.
- `Order summary` is your Module.

A Standard Module is a built-in, such as opening a URL or closing the browser. Your scanned Modules are the application.

If two tests use the same dialog, they share the Module. If a wizard has three distinct screens, those are three Modules, even if one TestCase walks through all three.

## Control groups

A repeating block (a table row, a plan card) is a control group. You identify the group, then the fields inside it. You do not create "Plan card 1", "Plan card 2", "Plan card 3" as separate Modules. The TestCase, or a constraint, says which card.

## Done when

For a page you know well, you can list the Modules, the controls on each, and the identify-by property or anchor. You can also list the controls you refused to scan, and why.

Next: [Lesson 3](03-testcases-and-actionmodes.md).
