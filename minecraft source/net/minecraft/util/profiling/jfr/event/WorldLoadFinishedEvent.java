/*    */ package net.minecraft.util.profiling.jfr.event;
/*    */ 
/*    */ import jdk.jfr.Category;
/*    */ import jdk.jfr.Event;
/*    */ import jdk.jfr.EventType;
/*    */ import jdk.jfr.Label;
/*    */ import jdk.jfr.Name;
/*    */ import jdk.jfr.StackTrace;
/*    */ 
/*    */ @Name("minecraft.LoadWorld")
/*    */ @Label("Create/Load World")
/*    */ @Category({"Minecraft", "World Generation"})
/*    */ @StackTrace(false)
/*    */ public class WorldLoadFinishedEvent
/*    */   extends Event {
/*    */   public static final String EVENT_NAME = "minecraft.LoadWorld";
/* 17 */   public static final EventType TYPE = EventType.getEventType(WorldLoadFinishedEvent.class);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\event\WorldLoadFinishedEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */