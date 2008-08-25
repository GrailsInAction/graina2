class ImageController {

    ImageService imageService

    def index = { }



    def show = {
        if (params.id) {
            def image = imageService.getUserThumbnail(params.id)
            response.setContentLength(image.length)
            response.getOutputStream().write(image)
        } else {
            response.sendError(404)
        }

    }

    def tiny = {
        if (params.id) {
            def image = imageService.getUserTinyThumbnail(params.id)
            response.setContentLength(image.length)
            response.getOutputStream().write(image)
        } else {
            response.sendError(404)
        }

    }
}
