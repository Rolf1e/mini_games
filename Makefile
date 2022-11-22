# === COMPILATION ===
compile: 
	./mill __.compile

compile-server:
	./mill server.compile

# === TEST ===
test: test-server

test-server:
	./mill server.test
