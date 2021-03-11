lib:
	cargo build

test:
	cargo test

clippy:
	cargo clippy
		
vebugger:
	cargo build
	cp target/debug/connect-four-ai .
