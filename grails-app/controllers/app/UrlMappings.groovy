package app

class UrlMappings {

    static mappings = {

        post "/demoIssue" (controller: 'application', action: 'demoIssue')

        "/"(controller: 'application', action: 'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
