/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.math;

/**
 *
 * @author gpetkos
 */
public class MathOps {

    float euclideanDistance(float[] vec1,float[] vec2){
        float dist=0;
        int i;
        int n_dim1=vec1.length;
        int n_dim2=vec2.length;
        if(n_dim1!=n_dim2)
            return -1;
        for(i=0;i<n_dim1;i++) 
            dist=dist+(vec1[i]-vec2[i])*(vec1[i]-vec2[i]);
        return (float) Math.sqrt(dist);
    }

    float vecLength(float[] vec){
        float len=0;
        int i;
        int n_dim=vec.length;
        for(i=0;i<n_dim;i++) 
            len=len+vec[i]*vec[i];
        return (float) Math.sqrt(len);
    }
    
    
}
