# Lab 1. Worked login

This is a finished paper TestCase. Read it once, close it, and rewrite the tables from the scenario below. Then open it and compare.

Scenario: a valid user opens the sample site, signs in, and sees the heading "Home". An unknown user sees the error "Unknown user".

## Module `Login`

Scan only these controls.

| Control | What it is | Identify by |
|---|---|---|
| Username | Text input | `id` = `username` |
| Password | Text input | `id` = `password` |
| Sign in | Button | Inner text `Sign in`, tag button |
| Error | Message area | `id` = `login-error` |

Refused on purpose: header logo, language switch, footer links. No test steers them.

## Module `Home`

| Control | What it is | Identify by |
|---|---|---|
| Heading | Page title | Inner text, used for Verify and WaitOn |

## Configuration, not steps

Set these on the ExecutionList, not in the Module:

| TCP | Smoke value |
|---|---|
| Browser | Chrome |
| BaseUrl | The Academy sample URL |
| User | The sample user from the exercise |
| Password | The sample password, supplied when you run |

## TestCase `Valid user reaches Home`

| Folder | Module | Control | ActionMode | Value |
|---|---|---|---|---|
| Open | Standard. Open URL | Url | Input | `{CP[BaseUrl]}` |
| Sign in | Login | Username | Input | `{CP[User]}` |
| Sign in | Login | Password | Input | `{CP[Password]}` |
| Sign in | Login | Sign in | Input | `{CLICK}` |
| Check | Home | Heading | WaitOn | `Home` |
| Check | Home | Heading | Verify | `Home` |
| Close | Standard. Close browser | | | |

`Login.Error` is not on this TestCase. It stays on the Module for the next one.

## TestCase `Unknown user sees an error`

Same Open and Sign in folders, with `User` overridden to a name that does not exist. Then:

| Module | Control | ActionMode | Value |
|---|---|---|---|
| Login | Error | WaitOn | `Unknown user` |
| Login | Error | Verify | `Unknown user` |

No Home Module. The heading must not be required for this case.

## What to notice

- The locator `id=username` appears once, on the Module.
- Both TestCases drag the same Login Module.
- WaitOn and Verify are two rows. WaitOn is not an assertion with a timeout hidden inside it.
- The password column contains `{CP[Password]}`, not a password.

## Your rewrite

Without scrolling back, fill this in from the scenario.

```
Module Login controls:
1.
2.
3.
4.

Valid user, Check folder:
Control        ActionMode     Value
Heading        __________     __________
Heading        __________     __________

Unknown user, the assertion rows:
Control        ActionMode     Value
__________     __________     __________
__________     __________     __________
```

Answers are the tables above. If you missed WaitOn, do lesson 3 again before lesson 4.
