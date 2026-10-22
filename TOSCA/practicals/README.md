# Practicals

These run in Chrome against `sut/index.html`. The runner follows the Tosca names from the lessons. It is not Tosca Commander, and it will not scan a page with XScan. You write the Module selectors and the ActionModes yourself, then run them.

Sample shop users, both with password `sample-only`:

| User | Result |
|---|---|
| alex | Home, three plans, each with a trial line |
| returning | Home, plans without a trial line, plus "Due today" |
| any other name | Error text "Unknown user" |

From the repository root:

```bash
python3 TOSCA/practicals/run.py --all
python3 TOSCA/practicals/run.py TOSCA/practicals/01-valid-login/case.yaml
```

## Order

1. **Valid login.** `01-valid-login/case.yaml` is complete. Run it. Read the log and match each line to Input, WaitOn, Verify, and Buffer.
2. **Unknown user.** Edit `02-unknown-user/yours.yaml`. Replace both `REPLACE_WITH_THE_MESSAGE` values. Run that file. Compare with `solution.yaml` after your run is green.
3. **Repair the Module.** Run `03-repair-the-module/broken.yaml`. The runner expects this one to fail, because Username is "the first input" and a promo field was inserted above it. Read `solution.yaml` and say which file changed: the Module selectors, not the TestSteps.
4. **Plan sheet.** `04-plan-sheet/solution.yaml` is one template and three instances. Add a wrong price on one instance, run it, see the Verify fail, then put the price back.
5. **No second trial.** `05-no-second-trial/solution.yaml` signs in as `returning`. The assertion is the "Due today" line. The trial nodes are gone, so a Verify on `.trial` would fail.

Mark the lines in `../lab/progress.md` when your own edit goes green, not when you only run the solutions.
