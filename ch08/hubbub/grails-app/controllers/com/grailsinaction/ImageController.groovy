package com.grailsinaction

class PhotoUploadCommand {
    byte[] photo
    String loginId
}

class ImageController {

    def upload(PhotoUploadCommand puc) {
        def user = User.findByLoginId(puc.loginId)
        user.profile.photo = puc.photo
        redirect controller: "user", action: "profile", id: puc.loginId
    }

    def form() {
        // pass through to upload form
        [ userList : User.list() ]
    }

    def renderImage(String id) {
        def user = User.findByLoginId(id)
        if (user?.profile?.photo) {
            response.setContentLength(user.profile.photo.size())
            response.outputStream.write(user.profile.photo)
        } else {
            response.sendError(404)
        }
    }
    
}
