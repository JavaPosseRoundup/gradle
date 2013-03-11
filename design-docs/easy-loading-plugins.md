
This feature covers how to simplify declaration of plugin repositories and declaring plugin load
Loading a plugin is not applying it to a project but just loading a jar and its dependencies in the buildscript classpath

# Issues with current plugin load

1. When using `apply from : 'URL'` users cannot define username, password or proxy to access the remote script file.
2. In many environment the root of the above URL may change depending on the environment and the user. I mean that the actual file name that need to be apply to this specific buildscript is under the control of the build.gradle maintainer. But the full source of where it comes from may vary (location, version, ...).
3. Adding a repository where plugins can be downloaded should be straightforward, and declared *outside* of my build scripts. The repository root for plugin should be external to the build scripts.
4. I `may` want to be able to download plugin by name without declaring the whole group:module:version coordinates.
5. When declaring a jar dependency for the classpath of the buildscript, I need to be explicit about the version. I like to say `cpp:latest_integration` or `bintray:latest_release` and finally just `docbook`.
6. I want Gradleware and the community to be able to distribute plugins (and even core plugins) on a simpler and shorter lifecycle than Gradle major release cycles.
7. I want to be able to force load plugins to all gradle projects executed in a specific environment (User dev build, Dedicated build agent, ...) whitout touching the build.gradle scripts of any projects.

# Possible implementations

- Using `init.gradle`, it will be a possible to declare some global plugin repositories. These repositories are not used by default or automatically in the current state of Gradle DSL to avoid backward compatibility issue. The big difference here between a plugin repository and a `buildscript.repositories` is that they are global not just for a specific buildscript.
- The above plugin repositories should be a standard repository handler with all the current cool features of Gradle repository configuration and management.
- To solve the last point, having a `allbuildscripts` script block usable in the `init.gradle` will make this possible. I still think that a single repository handler for plugins and external URL resolver is a good idea.
- There should be a clean an easy DSL for *loading* a plugin in the classpath of the buildscript. May be the `load` keyword, look good to me. It'll look like `buildscript.load 'cpp:1.4'`. The load will use plugin repositories to find the good 'group:module:version' jar and dependecies to add to the classpath of the build script.
- The above may generate one of Maven issue: The default plugin group. In gradle, declaring a valid list of group names that can be used for plugins sounds clean and straightforward.
- The load parameter object could be extended to support nice version range, default core download from central Gradleware repo (`buildscript.load 'gradle:idea:latest_integration'`).
- For the `apply from: 'URL'` the URL could use one of the name of the plugin repository to download the file: `apply from: 'repo:gist:/gituser/repo/gradle/scripts/docs.gradle'` and so reuse the credentials define in the 'gist' repository.
