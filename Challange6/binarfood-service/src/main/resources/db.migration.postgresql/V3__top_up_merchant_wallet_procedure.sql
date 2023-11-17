create or replace procedure public.top_up_merchant_wallet(oId uuid)
language plpgsql
as $$
begin
with subquery as (
select
	w.id as walletId,
	sum(od.quantity * p.price) as totalprice
from
	merchants m
join merchant_details md on
	md.merchant_id = m.id
join wallets w on
	w.id = md.wallet_id
join products p on
	p.merchant_id = m.id
join order_details od on
	od.product_id = p.id
where
	od.order_id = oId
group by
	od.id,
	w.id
)
update
	wallets w
set
	balance = w.balance + s.totalprice
from
	subquery s
where
	w.id = s.walletid;

update
	orders
set
	completed = true
where
	id = oId;
commit;
end;$$