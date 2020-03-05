package MyFirstRobot;

import java.awt.Color;
import java.awt.geom.Point2D;

import robocode.*;


 public class MyFirstRobot extends AdvancedRobot {
     //private EnemyRobot enemy = new EnemyRobot();
     private AdvancedEnemyBot enemy = new AdvancedEnemyBot();
	 int moveDirection = 1 ; // para onde se move
	 int radarDirection = 1;
	 //static int i=0;
	 //static int teimosia=20; 
     public void run() {
 		setColors(Color.blue, Color.red, Color.white);
 		setAdjustRadarForGunTurn(true);      	 // mantém o radar parado enquanto giramos 
 		setAdjustGunForRobotTurn(true);
         while (true) {
             //ahead(100);
             //turnGunLeft(10);
             //back(100);
            // turnRadarRight ( Double . POSITIVE_INFINITY ) ; // continue girando o radar para a direita 
            // turnRadarRightRadians ( Double . POSITIVE_INFINITY ) ; 
             doRadar();
             doMove();
             doGun();
 			 execute();
             
         }
     }
     
     //enemy = e

     public void onScannedRobot(ScannedRobotEvent e) {
    	// rastreia o inimigo 
 		if ( enemy.none() || e.getDistance() < enemy.getDistance() - 120 ||
 				e.getName().equals(enemy.getName())) {

 			// atualiza a posição dele
 			enemy.update(e, this);
 		}
       // fire(3);
     }
 	public void doMove() {
		// foca no inimigo mas roda um pouco a direção
		setTurnRight(normalRelativeAngle(enemy.getBearing() + 90 - (15 * moveDirection)));

		// muda de direção se pararmos
		// se bater na parede muda tb faz o mesmo que o onhitwall
		if (getVelocity() == 0) {
			setMaxVelocity(8);
			moveDirection *= -1;
			setAhead(10000 * moveDirection);
		}
	}
	 void doGun() {
			if (enemy.none())
				return;
			double firePower = Math.min(400 / enemy.getDistance(), 3);
			double bulletSpeed = 20 - firePower * 3;
			long time = (long)(enemy.getDistance() / bulletSpeed);
			double futureX = enemy.getFutureX(time);
			double futureY = enemy.getFutureY(time);
			double absDeg = absoluteBearing(getX(), getY(), futureX, futureY);
			setTurnGunRight(normalRelativeAngle(absDeg - getGunHeading()));
			if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 10) {
				setFire(firePower);
			}
	}
	 
		double absoluteBearing(double x1, double y1, double x2, double y2) {
			double xo = x2-x1;
			double yo = y2-y1;
			double hyp = Point2D.distance(x1, y1, x2, y2);
			double arcSin = Math.toDegrees(Math.asin(xo / hyp));
			double bearing = 0;

			if (xo > 0 && yo > 0) { 
				bearing = arcSin;
			} else if (xo < 0 && yo > 0) { 
				bearing = 360 + arcSin; 
			} else if (xo > 0 && yo < 0) { 
				bearing = 180 - arcSin;
			} else if (xo < 0 && yo < 0) { 
				bearing = 180 - arcSin; 
			}

			return bearing;
		}
	
    /* public void onHitWall(HitWallEvent e){
    	  double bearing = e.getBearing(); //ve onde esta a parede
    	  turnRight(-bearing); //inverte o sentido do robo 
    	  ahead(100); //avanca para fora da parede
    	}
     */

     public double normalRelativeAngle(double angle) {
         if (angle > -180 && angle <= 180) {
             return angle;
         }
  
         double fixedAngle = angle;
         while (fixedAngle <= -180) {
             fixedAngle += 360;
         }
  
         while (fixedAngle > 180) {
             fixedAngle -= 360;
         }
  
         return fixedAngle;
     }
     
 	
 	void doRadar() {
 		if (enemy.none()) {
 			// anda a volta a procura do inimigo 
 			setTurnRadarRight(Double.POSITIVE_INFINITY);
 		} else {
 			// oscila o radar 
 			double turn = getHeading() - getRadarHeading() + enemy.getBearing();
 			turn += 30 * radarDirection;
 			setTurnRadarRight(normalRelativeAngle(turn));
 			radarDirection *= -1;
 		}
 	}

     

     
 
  
 }