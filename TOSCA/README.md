# TOSCA

Study folder for Tricentis Tosca. It assumes you already design tests and have automated them in code. The lessons skip testing theory and teach the Tosca object model.

Tosca Commander is a Windows application and needs a license. The practicals under `practicals/` run in Chrome on this machine and use the same ActionModes as the lessons. They are not Commander. The Academy exercises still need a license.

## Study order

1. [Practicals](practicals/README.md). Run practical 1, then finish practical 2 yourself.
2. [Lesson 1. The model](lessons/01-the-model.md)
3. [Lab 1. Worked login](lab/01-worked-login.md)
4. [Lesson 2. Modules and XScan](lessons/02-modules-and-xscan.md)
5. [Lesson 3. TestCases and ActionModes](lessons/03-testcases-and-actionmodes.md)
6. [Lesson 4. Buffers, parameters, and libraries](lessons/04-buffers-parameters-libraries.md)
7. [Lesson 5. Test design, API, and execution](lessons/05-test-design-api-execution.md)
8. [Lab 2. Plan selection exercise](lab/02-exercise-plan-selection.md)
9. [Cheat sheet](reference/cheatsheet.md)
10. [Progress](lab/progress.md)

## First sitting

From the repository root:

```bash
python3 TOSCA/practicals/run.py TOSCA/practicals/01-valid-login/case.yaml
```

Then edit `TOSCA/practicals/02-unknown-user/yours.yaml` and run that file until it prints PASS. The worked set is `python3 TOSCA/practicals/run.py --all`.

Official course, after a license is available: [Tricentis Tosca Fundamentals — Automating web application testing](https://academy.tricentis.com/tricentis-tosca-fundamentals-automating-web-application-testing) (formerly Automation Specialist Level 1). Do Academy exercises 01 through 11 before opening TestCaseDesign or API Scan.

## What this folder does not contain

No Tosca workspace, no license, and no application passwords. Do not paste credentials, card numbers, or a live checkout session into these notes.
