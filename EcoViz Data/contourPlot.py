import numpy as np
import matplotlib.pyplot as plt 

f = open("S6000-6000-256.elv",'r')

header = f.readline()
header = header.split(" ")
dimX = int(header[0])
dimY = int(header[1])
gridSpace = header[2]
lat = header[3]
elevations = []

plt.figure(num=None,figsize=(10,7))

#fig,ax = plt.subplots()
for y in range(0,dimY):
    elevations.append([])
    line = f.readline()
    line = line.split()
    for x in range (0,dimX):
        elevations[y].append(line[x])

x = np.linspace(0,255,256)
y = np.linspace(0,255,256)
X, Y = np.meshgrid(x, y)
#print(X,Y)
#print(elevations)
plt.contourf(x,y,elevations)

f = open('S6000-6000-256_undergrowth.pdb','r')
numSpecies = int(f.readline())
speciesInfo = []
plantVectors = []
xTreeCoords = []
yTreeCoords = []

for i in range(0,numSpecies):
    speciesInfo.append([])
    plantVectors.append([])
    speciesInfo[i].append(f.readline().split())
    numPlants = int(f.readline())
    for j in range(0,numPlants):
        #plantVectors[i].append([])
        plantVectors[i].append(f.readline().split())
        #print(plantVectors[i][j])
        xTreeCoords.append(float(plantVectors[i][j][0]))
        yTreeCoords.append(float(plantVectors[i][j][1]))

plt.plot(np.array(xTreeCoords),np.array(yTreeCoords),'ko',alpha = 0.1,ms=4)

f = open('S6000-6000-256_canopy.pdb','r')
numSpecies = int(f.readline())
speciesInfo = []
plantVectors = []
xTreeCoords = []
yTreeCoords = []

for i in range(0,numSpecies):
    speciesInfo.append([])
    plantVectors.append([])
    speciesInfo[i].append(f.readline().split())
    numPlants = int(f.readline())
    for j in range(0,numPlants):
        #plantVectors[i].append([])
        plantVectors[i].append(f.readline().split())
        #print(plantVectors[i][j])
        xTreeCoords.append(float(plantVectors[i][j][0]))
        yTreeCoords.append(float(plantVectors[i][j][1]))

#ax.plot(xTreeCoords[0],yTreeCoords[0],'go')
#print(xTreeCoords)
#print()
#print(yTreeCoords)
plt.plot(np.array(xTreeCoords),np.array(yTreeCoords),'wo')



plt.show()