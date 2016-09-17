#!/usr/bin/python
import numpy
from scipy import stats
from scipy.optimize import curve_fit
#import matplotlib as plt
import sys

## Various curve-fitting functions
def linear(x, m, c): # y = mx + c
    return m * x + c

def poly(x, cl, s):
    return numpy.sum(numpy.array([x**i * cl[i] for i in range(0,s)]))

def poly2(x, c2, c1, c0): # y = c2*x^2 + c1*x^1 + c0
    return c2 * x ** 2 + c1 * x + c0

def poly3(x, c3, c2, c1, c0):
    return c3 * x ** 3 + c2 * x ** 2 + c1 * x + c0

def poly4(x, c4, c3, c2, c1, c0):
    return c4 * x ** 4 + c3 * x ** 3 + c2 * x ** 2 + c1 * x + c0

def poly5(x, c5, c4, c3, c2, c1, c0):
    return c5 * x ** 5 + c4 * x ** 4 + c3 * x ** 3 + c2 * x ** 2 + c1 * x + c0

def logarithmic(x, m, c): # y = m * log(x) + c
    return m * numpy.log(x) + c

def genexp(x, base, m, c): # y = c * base^(mx)
    return c * base ** (m*x)

def exp(x, m, c): # y = c * e^(mx)
    return c * numpy.exp(m * x)

def power(x, m, c):
    return c * x ** m

curveTypes = [linear, poly2, logarithmic, genexp, exp, power]
curveNames = ["linear", "poly2", "logarithmic", "genexp", "exp", "power"]

## Calculate coefficient of determination
def calcR2(X, Y, func, params):
    y_mean = Y.mean()
    y_predicted = [ func(x, *params) for x in X ]
    SS_tot = numpy.sum((Y - y_mean) ** 2)
    SS_res = numpy.sum((Y - y_predicted) ** 2)
    r2 = 1 - (SS_res / SS_tot)
    return r2

def fitCurve(X, Y, func):
    fit, cov = curve_fit(func, X, Y, maxfev=6000)
    r2 = calcR2(X, Y, func, fit)
    return (fit, r2)

def getBestFit(X, Y):
    curveFitData = []
    bestCurve = 0.
    bestR2 = 0.
    for i in range(0, len(curveTypes)):
#        print("Trying curve '%s'" %(curveNames[i]))
        curveFitInfo = fitCurve(X, Y, curveTypes[i])
        curveFitData.append(curveFitInfo)
        if (curveFitInfo[1] > bestR2):
            bestR2 = curveFitInfo[1]
            bestCurve = i
#    curveFitData = [ fitCurve(X, Y, curve) for curve in curveTypes]
#    bestFitData = max(curveFitData, key = lambda t: t[1])
#    bestCurve = curveFitData.index(bestFitData)
    bestFitData = curveFitData[bestCurve]
    return (curveNames[bestCurve], bestFitData)

def getAllFit(X, Y):
    curveFitData = []
    for i in range(0, len(curveTypes)):
        print("Trying curve '%s'" %(curveNames[i]))
        curveFitInfo = fitCurve(X, Y, curveTypes[i])
        curveFitData.append(curveFitInfo)
    return curveFitData


if (len(sys.argv) < 2):
    print("Usage: %s <csv file>" %(sys.argv[0]))
    sys.exit(-1)

csvFile = sys.argv[1]

## Read csv file
arr = numpy.genfromtxt(csvFile, delimiter=",", skip_header=0)

## Process 'arr' to get X and Y arrays
X = arr.T[0]
Y = arr.T[1]

curveFitData = getAllFit(X, Y)
for i in range(0, len(curveTypes)):
    print("%s: %f, %s" %(curveNames[i], curveFitData[i][1], curveFitData[i][0]))

bestCurve, bestFitData = getBestFit(X, Y)

print("Best fitting curve: '%s' with following parameters:" %(bestCurve))
print("Coefficients: %s" %(bestFitData[0]))
print("R^2: %f" %(bestFitData[1]))

### Additional stuff for debugging
#slope, intercept, r_value, p_value, std_err = stats.linregress(X,Y)
#print("Linregress: [%f %f], R^2 = %f" %(slope, intercept, r_value*r_value))
#
#z = numpy.polyfit(X, Y, 1)
#print("Linear with polyfit: [%f %f], R^2 = %f" %(z[0], z[1], calcR2AndPrint(X, Y, linear, z)))
#
### Calc R^2 for excel values
#excelVals = [0.0069, 0.0155]
#print("Excel linear: [%f %f], R^2 = %f" %(excelVals[0], excelVals[1], calcR2AndPrint(X, Y, linear, excelVals)))
#
