rootProject.name = "example"

include("aem")
include("aem:common")
include("aem:sites")
include("aem:site.live")
include("aem:site.demo")

include("aem:assembly:app")
include("aem:assembly:full")

include("test:functional")
include("test:integration")
include("test:performance")
