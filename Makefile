intellij:
	./mill mill.scalalib.GenIdea/idea

compile-server:
	./mill server.compile

test-server:
	./mill server.test

compile: compile-server

test: test-server