-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        8.0.29 - MySQL Community Server - GPL
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  12.0.0.6468
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- 正在导出表  spring_security_with_jwt.sys_func 的数据：~1 rows (大约)
INSERT INTO `sys_func` (`id`, `name`, `url`, `pid`, `sort`) VALUES
	(1, '首页', '/index', 0, 0);

-- 正在导出表  spring_security_with_jwt.sys_role 的数据：~1 rows (大约)
INSERT INTO `sys_role` (`id`, `name`) VALUES
	(1, 'User');

-- 正在导出表  spring_security_with_jwt.sys_role_func 的数据：~1 rows (大约)
INSERT INTO `sys_role_func` (`id`, `role_id`, `func_id`) VALUES
	(1, 1, 1);

-- 正在导出表  spring_security_with_jwt.sys_user 的数据：~1 rows (大约)
INSERT INTO `sys_user` (`id`, `name`, `username`, `password`, `age`, `state`) VALUES
	(1, 'lifezq', 'lifezq', '$2a$10$JrW0b7PBgL9riq2mOAPbRuymlpCRJHVncbGnTjg7hB/PExyGy.YbW', 33, 1);

-- 正在导出表  spring_security_with_jwt.sys_user_role 的数据：~1 rows (大约)
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`) VALUES
	(1, 1, 1);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
