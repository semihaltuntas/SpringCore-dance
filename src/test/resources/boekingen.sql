insert into boekingen(naam, aantalTickets, festivalId) values
    ('test1', 1,(select id from festivals where naam = 'test1')),
    ('test2', 2,(select id from festivals where naam = 'test2'));
