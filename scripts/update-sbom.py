import json
import sys
from pathlib import Path


def main() -> None:
    with Path("sbom.json").open("r") as f:
        sbom = json.load(f)

    tool = json.loads(sys.stdin.read())

    sbom.setdefault("packages", []).extend(tool.setdefault("packages", []))
    sbom.setdefault("files", []).extend(tool.setdefault("files", []))
    sbom.setdefault("relationships", []).extend(tool.setdefault("relationships", []))

    with Path("sbom.json").open("w") as f:
        json.dump(sbom, f)


if __name__ == "__main__":
    main()
