lib:
	cargo build

test:
	cargo test

fmt:
	cargo fmt

clippy:
	cargo clippy
		
vebugger:
	cargo build
	cp target/debug/connect-four-ai .
