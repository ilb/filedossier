# filedossier
Файловое досье
==============

Структура Dossier:
------------------
```
Dossier
|
└─ code - код для поиска
└─ name - название
└─ DossierFile
  |
  └─ parent - родительское досье
  └─ code - код для поиска
  └─ name - название
  └─ mediaType
  └─ Store
    |
    └─ storeKey - ключ директории досье в хранилище 
    └─ storeRoot - путь хранилища досье
    └─ Representation
      |
      └─ parent - родительский DossierFile
```
Core:
-----
<p align="center">
<img alt="Entities" src="docs/entities.svg" width="750">
</p>
