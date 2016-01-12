delimiter //
drop procedure if exists change_customer //
create procedure change_customer (in padelcampus varchar(100))
begin
declare finish int default 0;
declare tab varchar(100);
declare cur_tables cursor for SELECT DISTINCT TABLE_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE COLUMN_NAME IN ('customer_ID') AND TABLE_SCHEMA='padelcampus';
declare continue handler for not found set finish = 1;
open cur_tables;
my_loop:loop
fetch cur_tables into tab;
if finish = 1 then
leave my_loop;
end if;

set @str = concat('update ', tab, ' set customer_id=1 where customer_id is null');
prepare stmt from @str;
execute stmt;
deallocate prepare stmt;
end loop;
close cur_tables;
end; //
delimiter ;
call change_customer('padelcampus')