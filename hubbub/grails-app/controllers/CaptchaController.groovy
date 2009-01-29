import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

class CaptchaController {

	private static final String SOURCECHARS = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'

	def index = {
		response.setContentType('image/png')
		response.setHeader('Cache-control', 'no-cache')

		// Generate and remember the Source Character string (6 characters)
		int l = SOURCECHARS.length()
		StringBuilder b = new StringBuilder()
		6.times {
		    int r = (int)(Math.random() * l)
		    b.append(SOURCECHARS.charAt(r))
		}

		final int height = 200
		final int width = 200
		final int space = 8

		System.setProperty('java.awt.headless', 'true')
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
		Graphics2D g2d = bufferedImage.createGraphics()
		Font font = new Font('Serif', Font.BOLD, 18)
		g2d.setFont(font)
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
		Rectangle2D fontRect = font.getStringBounds(b.toString(), g2d.getFontRenderContext())
		// Now, create a graphic 'space' pixels wider and taller than the the font
		bufferedImage = new BufferedImage((int)fontRect.getWidth() + space,
				(int)fontRect.getHeight() + space,
				BufferedImage.TYPE_INT_RGB)
		g2d = bufferedImage.createGraphics()
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
		g2d.setFont(font)

		// Draw the background
		g2d.setColor(Color.WHITE)
		g2d.fillRect(0, 0, width, height)

		// Draw the lines
		g2d.setColor(Color.GRAY)
		int x1
		int y1
		int x2
		int y2
		final int step = 10
		x1 = 0
		y1 = step
		x2 = step
		y2 = 0
		while (x1 < width || x2 < width || y1 < height || y2 < height) {
		    g2d.drawLine(x1, y1, x2, y2)
		    if (y1 < height) {
				x1 = 0
				y1 += step
		    }
		    else if (x1 < width) {
				y1 = height
				x1 += step
		    }
		    else {
				x1 = width
				y1 = height
		    }

		    if (x2 < width) {
				y2 = 0
				x2 += step
		    }
		    else if (y2 < height) {
				x2 = width
				y2 += step
		    }
		    else {
				y2 = height
				x2 = width
		    }
		}

		// Draw the String
		g2d.setColor(Color.BLACK)

		g2d.drawString(b.toString(), (int)(space/2), (int)(space/4) + (int)fontRect.getHeight())

		OutputStream out = response.getOutputStream()
		ImageIO.write(bufferedImage, 'PNG', out)
		out.close()

		session.setAttribute('captcha', b.toString())
	}
}
