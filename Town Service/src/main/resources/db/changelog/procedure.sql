create or replace function public.DateDiff(units varchar(30), start_t timestamp, end_t timestamp)
    returns int as
$$
declare
    diff_interval interval;
    diff          int = 0;
    years_diff    int = 0;
begin
    if units in ('yy', 'yyyy', 'year', 'mm', 'm', 'month') then
        years_diff = date_part('year', end_t) - date_part('year', start_t);

        if units in ('yy', 'yyyy', 'year') then
            return years_diff;
        else
            return years_diff * 12 + (date_part('month', end_t) - date_part('month', start_t));
        end if;
    end if;

    diff_interval = end_t - start_t;

    diff = diff + date_part('day', diff_interval);

    if units in ('wk', 'ww', 'week') then
        diff = diff / 7;
        return diff;
    end if;

    if units in ('dd', 'd', 'day') then
        return diff;
    end if;

    diff = diff * 24 + date_part('hour', diff_interval);

    if units in ('hh', 'hour') then
        return diff;
    end if;

    diff = diff * 60 + date_part('minute', diff_interval);

    if units in ('mi', 'n', 'minute') then
        return diff;
    end if;

    diff = diff * 60 + date_part('second', diff_interval);

    return diff;
end;
$$ language plpgsql;

create or replace procedure public.calc_new_consumer_power_and_counter_value(weather_town_id uuid, new_temp double precision)
    language plpgsql
as
$$
declare
    consumer                    record;
    new_counter_value           double precision;
    new_electric_consumer_power double precision;
begin

    for consumer in
        select ec.id                     as id,
               ec.electric_power         as power,
               ec.time_last_switch_on    as time_last_switch_on,
               ec.electric_consumer_type as type,
               house.wall_thickness      as house_wall_thickness,
               counter.id                as counter_id,
               counter.value             as counter_value
        from application.house house
                 join application.room room on house.id = room.house_id
                 join application.counter counter on house.id = counter.house_id
                 join application.room_electric_consumers rec on room.id = rec.room_id
                 join application.electric_consumer ec on ec.id = rec.electric_consumers_id
        where house.town_id = weather_town_id
          and (ec.electric_consumer_type = 'CONVERTER' or ec.electric_consumer_type = 'CONDITIONER')
          and counter.communal_type = 'ELECTRIC'
        loop

            new_counter_value =
                            datediff(
                                    'second',
                                    consumer.time_last_switch_on::timestamp,
                                    now()::timestamp) / 60 * consumer.power;

            update application.counter
            set value = value + new_counter_value
            where id = consumer.counter_id;

            if new_temp < 0.0 and consumer.type = 'CONVERTER' then
                /* converter on */

                new_electric_consumer_power =
                        round(((new_temp * -1) / consumer.house_wall_thickness)::numeric * 0.6, 2);

                update application.electric_consumer
                set electric_power      = new_electric_consumer_power,
                    time_last_switch_on = now()
                where id = consumer.id;

            elsif new_temp >= 0.0 and consumer.type = 'CONVERTER' then
                /* converter off */

                update application.electric_consumer
                set electric_power      = 0.0,
                    time_last_switch_on = now()
                where id = consumer.id;

            end if;

            if new_temp > 20.0 and consumer.type = 'CONDITIONER' then
                /* conditioner on */

                new_electric_consumer_power = new_temp * 2 * 0.6;

                update application.electric_consumer
                set electric_power      = new_electric_consumer_power,
                    time_last_switch_on = now()
                where id = consumer.id;

            elsif new_temp <= 20.0 and consumer.type = 'CONDITIONER' then
                /* conditioner off */

                update application.electric_consumer
                set electric_power      = 0.0,
                    time_last_switch_on = now()
                where id = consumer.id;

            end if;
        end loop;
end;
$$;

create or replace procedure public.calc_counter_value(houseId uuid)
    language plpgsql
as
$$
declare
    consumer          record;
    new_counter_value double precision;
begin

    for consumer in
        select ec.id                     as id,
               ec.electric_power         as power,
               ec.time_last_switch_on    as time_last_switch_on,
               ec.electric_consumer_type as type,
               house.wall_thickness      as house_wall_thickness,
               counter.id                as counter_id,
               counter.value             as counter_value
        from application.house house
                 join application.room room on house.id = room.house_id
                 join application.counter counter on house.id = counter.house_id
                 join application.room_electric_consumers rec on room.id = rec.room_id
                 join application.electric_consumer ec on ec.id = rec.electric_consumers_id
        where house.id = houseId
          and counter.communal_type = 'ELECTRIC'
        loop

            new_counter_value =
                            datediff(
                                    'second',
                                    consumer.time_last_switch_on::timestamp,
                                    now()::timestamp) / 60 * consumer.power;

            update application.counter
            set value = value + new_counter_value
            where id = consumer.counter_id;

            update application.electric_consumer
            set time_last_switch_on = now()
            where id = consumer.id;

        end loop;
end;
$$;