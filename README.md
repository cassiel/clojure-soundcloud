# SoundCloud for Clojure

Soundcloud connection for Clojure, using the
[java-api-wrapper][api]. This is pretty much a straight port of the
example code. All we've done so far is port the upload procedure, but
the rest should be straightforward.

## Usage

SoundCloud's Java API libraries are all available on the Maven global
repositories, so we just fetch everything in Leiningen. (See
`project.clj`.)

There's a tiny amount of local configuration: see `manifest.clj`. The
file named in `SERIALISED-WRAPPER` needs to be readable and writable:
the original example code serialises the authentication token here,
for reasons best known to itself. The file named in `CREDENTIALS`
holds the SoundCloud credentials in JSON format, like so:

        {
                "username" : "[username]",
                "password" : "[password]",
                "id" : "[application id]",
                "secret" : "[application secret]"
        }

Hack the paths in `scratch.clj`, and enjoy.

## License

Copyright © 2012 Nick Rothwell, nick@cassiel.com

Distributed under the Eclipse Public License, the same as Clojure.

[api]: https://github.com/soundcloud/java-api-wrapper
