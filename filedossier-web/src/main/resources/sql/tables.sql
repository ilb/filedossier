CREATE TABLE `dossierContext` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_dossierContext_1` FOREIGN KEY (`id`) REFERENCES `dossiercontextData` (`dossierContextId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `dossiercontextData` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `dossierContextId` int(11) DEFAULT NULL,
  `key` varchar(64) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_dossiercontextData_1_idx` (`dossierContextId`),
  CONSTRAINT `fk_dossiercontextData_1` FOREIGN KEY (`dossierContextId`) REFERENCES `dossierContext` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
