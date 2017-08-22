from SimpleCV import *
import pyautogui
import time

'''
Controls.py
author - Dominick Nguyen @ UCSD
date - 12 / 01 / 2015

Tracks your hand using computer vision. Uses your skin Color
to detect where your hand is. Draws rectangle to define regions
for direction controls.

'''

#Creates a line layer
def create_box(topLeft, length, width):
	layer = DrawingLayer((320,340))
	layer.rectangle( topLeft, (width, length), Color.BLUE, 2)
	return layer

#Creates a text layer
def create_text(string, topRight):
	drawinglayer = DrawingLayer((320,240))
	drawinglayer.text(string, topRight, Color.YELLOW)
	return drawinglayer

'''

Checks if the center of the blob is within
the move region for the corresponding direction.
If it is, returns direction of the program.
Will return NONE if center not in region.

'''

def get_move(skinBlob):
	centerPoint = skinBlob.centroid()
	x = centerPoint[0]
	y = centerPoint[1]
	# The line that represents the diagonal connected by points
	# (0,0) and (480, 640) is modeled by the function y = 4/3 x	
	# The line that represents the diagonal connected by points
	# (0, 480) and (640, 0) is modeled by the function y = 480 - 4/3 x
	if (0 < x < 70) and (80 < y < 150):
		return 'left'
	if (120 < x < 205) and (0 < y < 60):
		return 'up'
	if (235 < x < 320)  and ( 80 < y < 150): 
		return 'right'
	if (120< x < 205) and ( 170 < y < 240):
		return 'down'

def main():
	#Initializes camera and sets initial resolution
	cam = Camera(0, {"width": 320,"height": 240})
	display = Display()
	#When repeats action until user closes  window
	while not display.isDone():

		#If the user clicks the simpleCV window, it closes
		if display.mouseLeft:
			break

		#Takes picture and scans for hand
		image = cam.getImage().flipHorizontal()
		skinBlob = image.findSkintoneBlobs()

		'''
		Draws a bounding rectangle on top of hand so that we can
		see position of current hand. 

		The hand (assuming it is the largest skin patch of skin) 

		Note: If user is not recognized, will catch exception and let
		user know if no one is there.

		'''
		try:
			dlayer = DrawingLayer((320,240))
			dlayer.rectangle(skinBlob[-1].topLeftCorner(), (skinBlob[-1].width(),
			                 skinBlob[-1].height()), Color.RED, width = 5,
			                 filled = False, alpha = 128)
			image.addDrawingLayer(dlayer)
			
			# Adds green line for bounding spaces
			image.addDrawingLayer(create_box((120,0), 60, 85))
			image.addDrawingLayer(create_box((0, 80), 70, 70 ))
			image.addDrawingLayer(create_box((120, 180), 60, 85))
			image.addDrawingLayer(create_box((250, 80), 70, 70))
			'''
			# Adds text to show LEFT, RIGHT, UP, DOWN
			image.addDrawingLayer(create_text("LEFT", (0, 80)))
			image.addDrawingLayer(create_text("RIGHT",(530, 130)))
			image.addDrawingLayer(create_text("UP", (360, 170)))
			image.addDrawingLayer(create_text("DOWN", (360, 290)))
			'''
			# Gets move and presses key. See get_move
			# Move is only one when it's not in a square
			# Uses pyautogui to simulate key event
			move = get_move(skinBlob[-1])
			if move is not None:
				pyautogui.click()
				pyautogui.press(move)
			

			# Applies layers
			image.applyLayers()
		except TypeError:
			pass
		finally:
			image.show()
		
		# Sleeps to lower fps and prevent lag
		time.sleep(.1)

main()




