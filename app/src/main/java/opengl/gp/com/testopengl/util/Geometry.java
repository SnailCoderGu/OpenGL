package opengl.gp.com.testopengl.util;


import static opengl.gp.com.testopengl.util.Geometry.Ray.vectorBetween;

public class Geometry {
    public static class Point{
        public final float x,y,z;
        public Point(float x,float y,float z){
            this.x = x;
            this.y = y;
            this.z = z;
        }
        public Point translateY(float distance){
            return new Point(x,y+distance,z);
        }
        public Point translate(Vector vector){
            return new Point(x+vector.x,y+vector.y,z+vector.z);
        }

    }
    public static class Circle{
        public final Point center;
        public final float radius;

        public Circle(Point center,float radius){
            this.center = center;
            this.radius = radius;
        }
        public Circle scle(float scale){
            return new Circle(center,radius*scale);
        }
    }

    public static class Cylinder{
        public final Point center;
        public final float radius;
        public final float height;

        public Cylinder(Point center,float radius,float height){
            this.center = center;
            this.radius = radius;
            this.height = height;
        }
    }

    /**
     * 向量类
     */
    public static class Vector{
        public final float x,y,z;
        public Vector(float x,float y,float z){
            this.x = x;
            this.y = y;
            this.z = z;
        }
        public float length(){
            return (float)Math.sqrt(x*x+y*y+z*z);
        }
        public Vector crossProuduct(Vector other){
            return new Vector((y*other.z)-(z*other.y),(z*other.x)-(x*other.z),(x*other.y)-(y*other.x));
        }
        //计算向量之间的点积
        public float dotProduct(Vector other){
            return x*other.x + y*other.y+z*other.z;
        }
        //向量缩放
        public Vector scale(float f){
            return new Vector(x*f,y*f,z*f);
        }

    }

    /**
     * 一个射线的描述，包含一个点，和一个向量方向
     */
    public static class Ray{
        public final Point point;
        public final Vector vector;

        public Ray(Point point,Vector vector){
            this.point = point;
            this.vector = vector;
        }

        //两个点之间的向量距离
        public static Vector vectorBetween(Point from,Point to){
            return new Vector(to.x - from.x,to.y-from.y,to.z-from.z);
        }
    }


    /**
     * 一个圆球范围，包含一个点和一个半径距离
     */
    public static class Sphere{
        public final Point center;
        public final float radius;
        public Sphere(Point center,float radius){
            this.center = center;
            this.radius = radius;
        }
    }

    public static Vector vectorBetween(Point from, Point to) {
        return new Vector(
                to.x - from.x,
                to.y - from.y,
                to.z - from.z);
    }

    /**
     * y
     * @param point
     * @param ray
     * 一个点到一条射线之间的距离
     * @return
     */
    public static float distanceBetween(Point point,Ray ray){
        //两点之间的向量距离
        Vector p1ToPoint = vectorBetween(ray.point,point);//射线起点到一个点的向量距离
        Vector p2ToPoint = vectorBetween(ray.point.translate(ray.vector),point); //射线终点到一个点的距离

        float areaOfTriangleTimesTwo = p1ToPoint.crossProuduct(p2ToPoint).length();
        float lengthOfBase = ray.vector.length();

        float distanceFromPointToRay = areaOfTriangleTimesTwo/lengthOfBase;
        return distanceFromPointToRay;
    }
    public static boolean intersects(Sphere shere,Ray ray){
        return shere.radius >= distanceBetween(shere.center,ray)?true:false;
    }

    //平面,包含平面上的一个点和一个法向量
    public static class Plane{
        public final Point point;
        public final Vector normal;
        public Plane(Point point,Vector normal){
            this.point = point;
            this.normal = normal;
        }
    }

    /**
     * 计算一条射线与一个平面的哪个点相交
     */
    public static Point intersectionPoint(Ray ray,Plane plane){
        Vector rayToPlaneVector = vectorBetween(ray.point,plane.point);

        float scaleFactor = rayToPlaneVector.dotProduct(plane.normal)/ray.vector.dotProduct(plane.normal);

        Point intersectionPoint = ray.point.translate(ray.vector.scale(scaleFactor));
        return intersectionPoint;
    }




}
