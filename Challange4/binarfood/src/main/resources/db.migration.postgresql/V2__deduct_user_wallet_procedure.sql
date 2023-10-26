create or replace procedure public.deduct_user_wallet(oId uuid, uname varchar, pwd varchar)
language plpgsql
as $$
begin
with subquery as (
select
		o.id as orderId ,
		w.id as walletId,
		sum(od.quantity * p.price) as totalprice
from
		users u
join user_details ud on
		ud.user_id = u.id
join wallets w on
		w.id = ud.wallet_id
join orders o on
		o.user_id = u.id
join order_details od on
		o.id = od.order_id
join products p on
		p.id = od.product_id
where
		o.id = oId
	and u.username = uname
	and u.password = pwd
group by
		o.id,
		w.id
)
update
	wallets w
set
	balance = w.balance - s.totalprice
from
	subquery s
where
	w.id = s.walletid;
commit;
end;$$