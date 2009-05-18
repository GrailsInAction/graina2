package com.grailsinaction

class PhotoUploadCommand {
    byte[] photo
    String userId
}

class ImageController {

    def imageService

    def upload = { PhotoUploadCommand puc ->
        println "Starting upload"
        def user = User.findByUserId(puc.userId)
        user.profile.photo = puc.photo
        redirect(controller: 'user', action: 'profile', id: puc.userId)
    }

    def rawUpload = {

        // a Spring MultipartHttpServletRequest
        def mhsr = request.getFile('photo')
        if(!mhsr?.empty && mhrs.size < 1024*200) { // 200kb
            mhsr.transferTo(
                new File('/hubbub/images/${request.userId}/mugshot.gif') )

        }
    }

    def form = {
        // pass through to upload form
    }

    def view = {
        // path through to "view photo" page
    }

    def renderImage = {
        def user = User.findByUserId(params.id)
        if (user && user?.profile?.photo) {
            response.setContentLength(user.profile.photo.length)
            response.getOutputStream().write(user.profile.photo)
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
