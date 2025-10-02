#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

REPO_ROOT=$(git rev-parse --show-toplevel)

# Generate SBOM for current directory
syft -o spdx-json . > "$REPO_ROOT/sbom.json"

# Generate and merge SBOMs for each tool passed as argument
for tool in "$@"; do
  echo "Creating SBOM for $tool and merging"
  tool_path=$(command -v "$tool")
  if [[ -z "$tool_path" ]]; then
    echo "Warning: '$tool' not found in PATH. Skipping." >&2
    continue
  fi
  syft -q -o spdx-json "$tool_path" | python "$REPO_ROOT/scripts/update-sbom.py"
done