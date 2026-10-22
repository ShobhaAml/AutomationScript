#!/usr/bin/env python3
"""Run Tosca-shaped practicals against the local sample shop.

This is a practice runner. It is not Tricentis Tosca Commander.
Action names match the lessons: Input, Verify, WaitOn, Buffer, Constraint.
{CP[Name]} and {B[Name]} match Tosca. {Sheet[Column]} stands in for the
attribute link Commander creates when you drag a TestSheet column onto a
TestStepValue.
"""

from __future__ import annotations

import argparse
import re
import sys
from pathlib import Path

import yaml
from playwright.sync_api import expect, sync_playwright

ROOT = Path(__file__).resolve().parent
SUT = ROOT / "sut" / "index.html"
TOKEN = re.compile(r"\{(CP|B|Sheet)\[([^\]]+)\]\}")
SOLUTIONS = [
    ROOT / "01-valid-login" / "case.yaml",
    ROOT / "02-unknown-user" / "solution.yaml",
    ROOT / "03-repair-the-module" / "broken.yaml",
    ROOT / "03-repair-the-module" / "solution.yaml",
    ROOT / "04-plan-sheet" / "solution.yaml",
    ROOT / "05-no-second-trial" / "solution.yaml",
]


def resolve(value, tcp, buffers, sheet):
    if not isinstance(value, str):
        return value

    def replacer(match):
        kind, name = match.group(1), match.group(2)
        source = {"CP": tcp, "B": buffers, "Sheet": sheet or {}}[kind]
        if name not in source:
            raise KeyError(f"{kind}[{name}] is not set")
        return str(source[name])

    return TOKEN.sub(replacer, value)


def control_spec(modules, module_name, control_name):
    try:
        spec = modules[module_name][control_name]
    except KeyError as exc:
        raise KeyError(f"{module_name}.{control_name} is not in the Module") from exc
    if isinstance(spec, str):
        return {"selector": spec}
    return spec


def locator_for(page, scopes, modules, module_name, control_name):
    spec = control_spec(modules, module_name, control_name)
    parent = scopes.get(module_name, page)
    return parent.locator(spec["selector"])


def log_value(control, value):
    if control.lower() == "password":
        return "••••"
    return value


def run_instance(page, case, sheet_row, logs):
    tcp = dict(case.get("tcp") or {})
    buffers = {}
    scopes = {}
    modules = case.get("modules") or {}
    label = ""
    if sheet_row:
        label = " | ".join(f"{key}={value}" for key, value in sheet_row.items())
        logs.append(f"instance {label}")
    for step in case["steps"]:
        action = step["action"]
        if action == "Open":
            query = case.get("sut_query")
            url = SUT.resolve().as_uri()
            if query:
                url = f"{url}?{query}"
            page.goto(url)
            logs.append("Open sample shop")
            continue
        module_name = step["module"]
        control = step["control"]
        raw_value = step.get("value", "")
        value = resolve(raw_value, tcp, buffers, sheet_row)
        if action == "Constraint":
            spec = control_spec(modules, module_name, control)
            attr = spec.get("match_attr", "data-plan")
            scopes[module_name] = page.locator(f'[{attr}="{value}"]')
            logs.append(f"Constraint {control} = {log_value(control, value)}")
            continue
        target = locator_for(page, scopes, modules, module_name, control)
        shown = log_value(control, value)
        if action == "Input":
            if value == "{CLICK}":
                target.click()
            else:
                target.fill(str(value))
            logs.append(f"Input {control} = {shown}")
        elif action == "WaitOn":
            expect(target).to_be_visible(timeout=5000)
            expect(target).to_have_text(str(value), timeout=5000)
            logs.append(f"WaitOn {control} = {shown}")
        elif action == "Verify":
            expect(target).to_be_visible(timeout=2000)
            expect(target).to_have_text(str(value), timeout=2000)
            logs.append(f"Verify {control} = {shown}")
        elif action == "Buffer":
            buffers[str(value)] = target.inner_text().strip()
            logs.append(f"Buffer {value} = {buffers[str(value)]}")
        else:
            raise ValueError(f"Unsupported ActionMode {action}")
    return buffers


def run_case(case_path):
    case = yaml.safe_load(case_path.read_text())
    logs = [case["name"]]
    error = None
    with sync_playwright() as playwright:
        browser = playwright.chromium.launch(
            channel="chrome",
            headless=True,
            args=["--no-sandbox", "--disable-dev-shm-usage"],
        )
        page = browser.new_page()
        try:
            instances = case.get("instances") or [None]
            for row in instances:
                run_instance(page, case, row, logs)
        except Exception as exc:  # noqa: BLE001 - reported as the practical result
            error = exc
        finally:
            browser.close()
    expect_fail = case.get("expect") == "fail"
    if error and not expect_fail:
        logs.append(f"FAIL {error}")
        return False, logs
    if error and expect_fail:
        logs.append(f"FAIL as expected: {error.__class__.__name__}")
        return True, logs
    if expect_fail:
        logs.append("FAIL The broken Module passed. Identification did not break.")
        return False, logs
    logs.append("PASS")
    return True, logs


def main(argv):
    parser = argparse.ArgumentParser(description="Run a Tosca-shaped practical")
    parser.add_argument("cases", nargs="*", type=Path)
    parser.add_argument("--all", action="store_true", help="Run the worked practicals")
    args = parser.parse_args(argv)
    paths = SOLUTIONS if args.all or not args.cases else args.cases
    failed = False
    for path in paths:
        ok, logs = run_case(path)
        print("\n".join(logs))
        print()
        failed = failed or not ok
    return 1 if failed else 0


if __name__ == "__main__":
    sys.exit(main(sys.argv[1:]))
