/*
SQLyog Ultimate v11.24 (32 bit)
MySQL - 5.6.15-ndb-7.3.4 : Database - memFox
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`memFox` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `memFox`;

/*Table structure for table `memData` */

DROP TABLE IF EXISTS `memData`;

CREATE TABLE `memData` (
  `dataID` int(11) NOT NULL AUTO_INCREMENT,
  `groupID` int(11) NOT NULL,
  `hostID` int(11) NOT NULL,
  `paramName` varchar(80) NOT NULL,
  `paramValue` varchar(80) NOT NULL,
  KEY `dataID` (`dataID`),
  KEY `dataUniq` (`hostID`,`paramName`),
  KEY `groupFK` (`groupID`),
  CONSTRAINT `groupFK` FOREIGN KEY (`groupID`) REFERENCES `memGroup` (`groupID`),
  CONSTRAINT `hostFK` FOREIGN KEY (`hostID`) REFERENCES `memHostList` (`hostid`)
) ENGINE=InnoDB AUTO_INCREMENT=9584 DEFAULT CHARSET=latin1;

/*Table structure for table `memGroup` */

DROP TABLE IF EXISTS `memGroup`;

CREATE TABLE `memGroup` (
  `groupID` int(11) NOT NULL AUTO_INCREMENT,
  `groupName` varchar(50) DEFAULT NULL,
  KEY `groupID` (`groupID`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;

/*Table structure for table `memHistory` */

DROP TABLE IF EXISTS `memHistory`;

CREATE TABLE `memHistory` (
  `historyID` int(11) NOT NULL AUTO_INCREMENT,
  `hostID` int(11) DEFAULT NULL,
  `paramName` varchar(50) DEFAULT NULL,
  `paramValue` double DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  KEY `historyID` (`historyID`)
) ENGINE=InnoDB AUTO_INCREMENT=7560 DEFAULT CHARSET=latin1;

/*Table structure for table `memHostList` */

DROP TABLE IF EXISTS `memHostList`;

CREATE TABLE `memHostList` (
  `hostid` int(11) NOT NULL AUTO_INCREMENT,
  `groupID` varchar(30) NOT NULL,
  `hostName` varchar(60) NOT NULL,
  `hostIP` varchar(18) NOT NULL,
  `hostPort` int(11) NOT NULL,
  `lastUpdate` datetime DEFAULT NULL,
  UNIQUE KEY `svrUniq` (`hostIP`,`hostPort`),
  KEY `hostid` (`hostid`)
) ENGINE=InnoDB AUTO_INCREMENT=231 DEFAULT CHARSET=latin1;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
