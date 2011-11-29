update yfs_category_domain set category_domain='MasterCatalog' where category_domain_key = (select category_domain_key from yfs_category_domain where category_domain='xpedxMasterCatalog' and organization_code='xpedx');
commit;

update yfs_category set category_path = replace(category_path,'xpedxMasterCatalog','MasterCatalog');
commit;