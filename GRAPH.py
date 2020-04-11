from mpl_toolkits.mplot3d import Axes3D
import matplotlib.pyplot as plt



fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')


x=[3.4,6.8,10.2,13.6,17,20.4,23.8,27.2,30.6,34,37.4,40.8,44.2,47.6,51,54.4,57.8,61.2]
z=[700,1400,2100,2800,3500,4200,4900,5600,6300,7000,7700,8400,9100,9800,10500,11200,11900,12600]
y =[5.4,10.8,16.2,21.6,27,32.4,37.8,43.2,48.6,59.4,64.8,70.2,75.6,81,86.4,91.8,97.2,100]

ax.scatter(x, y, z, c='r', marker='o')


ax.set_xlabel('increase of pollution')
ax.set_ylabel('increase in factor of pollution')
ax.set_zlabel('Money spent in removing trees')
plt.title('Pollution')

plt.show()


