compile: compile-server

intellij:
	./mill mill.scalalib.GenIdea/idea

compile-server:
	./mill server.compile

test-server:
	./mill server.test


test: test-server
