with Ada.Text_IO;

procedure Test is

   can_stop : boolean := false;
   pragma Atomic(can_stop);

   task type break_thread;
   task type main_thread(id, step: Integer);

   task body break_thread is
   begin
      delay 2.0;
      can_stop := true;
   end break_thread;

   task body main_thread is
      sum : Long_Long_Integer := 0;
      current : Long_Long_Integer := 0;
   begin
      loop
      current := current + Long_Long_Integer(step);
         sum := sum + current;
         exit when can_stop;
      end loop;
      delay 1.0;

      Ada.Text_IO.Put_Line("Thread" & id'Img & ": Sum from 0 to" & current'Img & " by" 
                            & step'Img & " (" & Long_Long_Integer'Image(current / Long_Long_Integer(step)) 
                            & " elements) is" & sum'Img);
   end main_thread;

   b1 : break_thread;
   t1 : main_thread(1, 2);
   t2 : main_thread(2, 6);
   t3 : main_thread(3, 9);
   t4 : main_thread(4, 10);
begin
   null;
end Test;