package com.grailsinaction

import java.awt.image.BufferedImage
import javax.imageio.ImageIO

import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import java.io.ByteArrayInputStream

class ImageService {

    // image scaling stuff from http://www.velocityreviews.com/forums/t148931-how-to-resize-a-jpg-image-file.html
    byte[] scale(byte[] srcFile, int destWidth, int destHeight) throws IOException {
        BufferedImage src = ImageIO.read(new ByteArrayInputStream(srcFile));
        BufferedImage dest = new BufferedImage(destWidth, destHeight,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = dest.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance(
                (double) destWidth / src.getWidth(),
                (double) destHeight / src.getHeight());
        g.drawRenderedImage(src, at);
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ImageIO.write(dest, "JPG", baos);
        return baos.toByteArray()
    }


    byte[] getUserThumbnail(String userId) {
        def user = User.findByUserId(userId)
        def image = user.profile.photo
        if (!image) {
            log.debug "No profile pic found, using default image"
            def url = this.class.getResource("/default_user.jpg")
            image = new File(url.toURI()).readBytes()
        }
        return image

    }

    byte[] getUserTinyThumbnail(String userId) {
        def image = getUserThumbnail(userId)
        image = scale(image, 24, 24)
        return image
    }







}
