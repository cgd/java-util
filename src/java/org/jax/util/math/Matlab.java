/*
 * Copyright (c) 2009 The Jackson Laboratory
 * 
 * This software was developed by Gary Churchill's Lab at The Jackson
 * Laboratory (see http://research.jax.org/faculty/churchill).
 *
 * This is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jax.util.math;

import java.util.Arrays;
import java.util.Random;

/**
 * This class implements MATLAB functions used in MAANOVA files The methods
 * names mimics original MATLAB function in most cases. However, sometimes I
 * used reduced version of function. In those cases special name have been
 * assigned for method. I alsoincluded in this class as method the functions
 * which do not have there MATLAB conterpart,(for innst. CATEGORY)
 * @author Hao Wu & Lei Wu
 */
// suppressing all warnings because this is older code
@SuppressWarnings("all")
public class Matlab{
  // writen by Lei
  public static double fixDecimalRound(double number, int numDigits) {
//    return number;
    return Math.round(number * Math.pow(10, numDigits))/Math.pow(10, numDigits);
  }

  public static int[] abs(int[] arr) {
    int[] result = new int[arr.length];
    for(int i=0; i<arr.length; i++)
      result[i] = Math.abs(arr[i]);
    return result;
  }

  public static double[] abs(double[] arr) {
    double[] result = new double[arr.length];
    for(int i=0; i<arr.length; i++)
      result[i] = Math.abs(arr[i]);
    return result;
  }

  public static boolean any(boolean[] arr) {
    for(int i=0; i<arr.length; i++)
      if(arr[i]) return true;
    return false;
  }

  public static int[] find(boolean[] arr) {
    int n = arr.length;
    int[] out = new int[n];
    int count = 0;
    for(int i=0; i<n; i++) {
      if(arr[i]) {
        out[count] = i;
        count ++;
      }
    }
    int[] result = new int[count];
    System.arraycopy(out, 0, result, 0, count);
    return result;
  }


  public static int[] find(int[] arr) {
    int n = arr.length;
    int[] out = new int[n];
    int count = 0;
    for(int i=0; i<n; i++) {
      if(arr[i] != 0) {
        out[count] = i;
        count ++;
      }
    }
    int[] result = new int[count];
    System.arraycopy(out, 0, result, 0, count);
    return result;
  }

  public static int[] find(int[] arr, int th, String op) {
    int n = arr.length;
    int[] out = new int[n];
    int count = 0;
    for(int i=0; i<n; i++) {
      if(op == ">") {
        if(arr[i] > th) {
          out[count] = i;
          count ++;
        }
      }
      else if(op == ">=") {
        if(arr[i] >= th) {
          out[count] = i;
          count ++;
        }
      }
      else if(op == "==") {
        if(arr[i] == th) {
          out[count] = i;
          count ++;
        }
      }
      else if(op == "<") {
        if(arr[i] < th) {
          out[count] = i;
          count ++;
        }
      }
      else if(op == "<=") {
        if(arr[i] <= th) {
          out[count] = i;
          count ++;
        }
      }
    }
    int[] result = new int[count];
    System.arraycopy(out, 0, result, 0, count);
    return result;
  }

  public static int[] find(double[] arr, double th, String op) {
    int n = arr.length;
    int[] out = new int[n];
    int count = 0;
    for(int i=0; i<n; i++) {
      if(op == ">") {
        if(arr[i] > th) {
          out[count] = i;
          count ++;
        }
      }
      else if(op == ">=") {
        if(arr[i] >= th) {
          out[count] = i;
          count ++;
        }
      }
      else if(op == "==") {
        if(arr[i] == th) {
          out[count] = i;
          count ++;
        }
      }
      else if(op == "<") {
        if(arr[i] < th) {
          out[count] = i;
          count ++;
        }
      }
      else if(op == "<=") {
        if(arr[i] <= th) {
          out[count] = i;
          count ++;
        }
      }
    }
    int[] result = new int[count];
    System.arraycopy(out, 0, result, 0, count);
    return result;
  }

  public static  int IsMember(int a,int[] A)
  {
    int ret=0;// has to be 0 , but for debugging set to 1
    for(int i=0;i<A.length;i++){
      if(a==A[i])
        return ret=1;
    }
    return ret;
  }

  public static  int[] ModInt(int[] A,int a)
  {
    int l=A.length;
    int temp;
    int[] ret=new int[l];
    for(int i=0; i<l;i++){
      ret[i]=A[i]%a;
    }
    return ret;
  }

  public static  int SumInt(int[] A)
  {
    int ret=0;
    for(int i=0;i<A.length;i++)
      ret=ret+A[i];
    return ret;
  }

  public static  int SumInt(int[] A,int a)
  {
    int ret=0;
    for(int i=0;i<A.length;i++){
      if(A[i]==a)
        ret=ret+A[i];
    }
    return ret;
  }

  public static int Sum(boolean[] arr) {
    int sum = 0;
    for(int i=0; i<arr.length; i++)
      if(arr[i]) sum ++;
    return sum;
  }

  public static double Sum(double[] arr)
  {
    // this will sum only vector array in Matrix form
    double Sum=0;
    //double[] arr=M.getColumnPackedCopy();
    for(int i=0;i<arr.length;i++){
      Sum=Sum+arr[i];
    }

    return Sum;
  }

  public static  double Sum(int[] arr)
  {
    // this will sum only vector array in Matrix form
    double Sum=0;
    //double[] arr=M.getColumnPackedCopy();
    for(int i=0;i<arr.length;i++){
      Sum=Sum+arr[i];
    }

    return Sum;
  }

  public static  double Mean(double[] arr)
  {
    double mean=0;
    double Sum=0;
    //double[] arr=M.getColumnPackedCopy();
    for(int i=0;i<arr.length;i++){
      Sum=Sum+arr[i];
    }
    mean=Sum/arr.length;
    return mean;
  }

  public static  double SumResSq(double[] arr)
  {
    double Sum=0;
    double SR=0;
    double mean=0;
    //double[] arr=M.getColumnPackedCopy();
    for(int i=0;i<arr.length;i++){
      Sum=Sum+arr[i];
    }
    mean=Sum/arr.length;
    for(int i=0;i<arr.length;i++){
      SR=SR+Math.pow(arr[i]-mean,2);
    }

    return SR;
  }

  public static  double SumSq(double[] arr)
  {
    double S=0;
    for(int i=0;i<arr.length-1;i++){
      S=S+Math.pow(arr[i],2);
    }
    return S;
  }

  public static  double[] CastInt2Double(int[] arr)
  {
    // this is not MTALAB function; I nned  it rto cast conviniently int array to double
    double[] r;
    r=new double[arr.length];
    for(int i=0;i<arr.length;i++){
      r[i]=(double)arr[i];
    }
    return r;
  }

  public static  int[] CastDouble2Int(double[] arr)
  {
    int[] r;
    r=new int[arr.length];
    for(int i=0;i<arr.length;i++)
    {
      r[i]=(int)arr[i];
    }
    return r;
  }

  public static double Max(double[][] arr) {
    int n = arr.length;
    double[] tmp = new double[n];
    for(int i=0; i<n; i++)
      tmp[i] = Max(arr[i]);
    return Max(tmp);
  }

  public static double Max(double[] arr)
  {
    // return maximum element  of double array
    double r;
    double[] t=new double[arr.length];
    System.arraycopy(arr, 0, t, 0, arr.length);
    Arrays.sort(t);
    double m = 0.0;
    for(int i=t.length-1; i>=0; i--) {
      m = t[i];
      if(!Double.isNaN(m))
        break;
    }
    return m;
  }

  public static  int Max(int[] arr)
  {
    // return maximum element of int array
    int r;
    int[] t=new int[arr.length];
    System.arraycopy(arr, 0, t, 0, arr.length);
    Arrays.sort(t);
    return t[t.length-1];
  }

  public static double Min(double[][] arr) {
    int n = arr.length;
    double[] tmp = new double[n];
    for(int i=0; i<n; i++)
      tmp[i] = Min(arr[i]);
    return Min(tmp);
  }

  public static  double Min(double[] arr)
  {
    // return maximum element  of double array
    double r;
    double[] t=new double[arr.length];
    System.arraycopy(arr, 0, t, 0, arr.length);
    Arrays.sort(t);
    return t[0];
  }

  public static  int Min(int[] arr)
  {
    // return maximum element of int array
    int r;
    int[] t=new int[arr.length];
    System.arraycopy(arr, 0, t, 0, arr.length);
    Arrays.sort(t);
    return t[0];
  }
  
  public static int[] RandPerm(int n)
  {
    int[] r=new int[n];
    int k, temp;
    for(int i=0; i<n; i++)
      r[i] = i;
    Random rnd=new Random();
    for(int i=0; i<n; i++){
      k = rnd.nextInt(n-i);
      // exchange the position of r[k] and r[n-i-1]
      temp = r[k];
      r[k] = r[n-i-1];
      r[n-i-1] = temp;
    }
    return r;
  }

  public static int[] UniDRnd(int N, int m)
  {
    int[] arr=new int[N];
    Random rnd=new Random();
    for(int i=0;i<N;i++){
      arr[i] = rnd.nextInt(m);
    }
    return arr;
  }

  public static  double[] linspace(double lolim, double hilim, int n) {
    double[] c = new double[n];
    for(int i=0; i<n; i++)
      c[i] = lolim + i*(hilim-lolim)/(n-1);
    return(c);
  }

  public static int factorial(int n) {
    int result = 1;
    for(int i=1; i<=n; i++)
      result *= i;
    return(result);
  }

  // find the intersect of two integer sets
  public static int[] intersect(int[] set1, int[] set2) {
    int[] longset, shortset, tmpset;
    int n, count=0;
    if(set1.length > set2.length) {
      longset = (int[])set1.clone();
      shortset = (int[])set2.clone();
      n = set2.length;
    }
    else {
      longset = (int[])set2.clone();
      shortset = (int[])set1.clone();
      n = set1.length;
    }
    // sort longset
    Arrays.sort(longset);
    tmpset = new int[shortset.length];
    // loop for all elements in shortset
    for(int i=0; i<shortset.length; i++) {
      if( Arrays.binarySearch(longset, shortset[i]) >= 0 ) {
        // this element in shortset is found in longset
        tmpset[count] = shortset[i];
        count ++;
      }
    }
    // make return array
    int[] result = new int[count];
    for(int i=0; i<count; i++)
      result[i] = tmpset[i];

    return result;
  }

  /**
   * Calculate the standard deviation
   * @param x
   * @return
   */
  public static double std(double[] x) {
    double mx = Mean(x);
    double var = 0.0;
    for(int i=0; i<x.length; i++)
      var = var + (x[i]-mx)*(x[i]-mx);
    var = var / (x.length-1);
    double sd = Math.sqrt(var);
    return(sd);
  }

  /**
   * sort the input integer array in ascending order and return the index
   */
  public static int[] order(int[] x) {
    int n = x.length, index;
    // init result
    int[] idx = new int[n];
    for(int i=0; i<n; i++)
      idx[i] = i;
    // start to order
    for(int i=0; i<n; i++) {
      index = i;
      // find the smallest
      for (int j=i+1; j<n; j++)
        if (x[idx[j]] < x[idx[index]])
          index = j;
      idx[i] = index;
      idx[index] = i;
    }
    return(idx);
  }

  /**
   * sort the input double array in ascending order and return the index
   */
  public static int[] order(double[] x) {
    int n = x.length, index;
    // init result
    int[] idx = new int[n];
    for(int i=0; i<n; i++)
      idx[i] = i;
    // start to order
    for(int i=0; i<n; i++) {
      index = i;
      // find the smallest
      for (int j=i+1; j<n; j++)
        if (x[idx[j]] < x[idx[index]])
          index = j;
      // swap
      int temp = idx[i];
      idx[i] = idx[index];
      idx[index] = temp;
    }
    return(idx);
  }

  // extract the unique elements of an integer array
  public static int[] unique(int[] arr) {
    int n = arr.length;
    int[] t=new int[n], t2 = new int[n], result;
    int idx=0;
    System.arraycopy(arr, 0, t, 0, n);
    Arrays.sort(t);
    t2[0] = t[0]; idx ++;
    for(int i=1; i<n; i++) {
      if(t[i] != t[i-1]) {
        t2[idx] = t[i];
        idx ++;
      }
    }
    result = new int[idx];
    System.arraycopy(t2, 0, result, 0, idx);
    return(result);
  }

  public static int mod(int x, int y) {
    int tmpi;
    tmpi = (int)Math.floor(x/y);
    return x-tmpi*y;
  }

}// end of MATLAB
