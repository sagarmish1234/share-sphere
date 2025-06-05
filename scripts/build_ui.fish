#!/usr/bin/fish

# Install unzip if needed (uncomment if required)
# sudo apt install unzip

# Check Bun version
set BUN_VERSION (bun --version 2>/dev/null)
set RETURN_CODE $status
echo "Bun version return code $RETURN_CODE"

if test $RETURN_CODE -ne 0
    echo "Installing bun runtime"
    curl -fsSL https://bun.sh/install | bash
end

# Set environment variables
set -gx BUN_INSTALL "$HOME/.bun"
set -gx PATH "$BUN_INSTALL/bin:$PATH"

# Change to frontend directory and perform build
cd ../frontend/
bun install
bun run build
mkdir -p ../backend/target/classes/static/
rm -rf ../backend/target/classes/static/*
mv dist/* ../backend/target/classes/static/
