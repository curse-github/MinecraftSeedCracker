/*    */ package net.minecraft.server.level;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class Ticket {
/* 11 */   public static final MapCodec<Ticket> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BuiltInRegistries.TICKET_TYPE
/* 12 */         .byNameCodec().fieldOf("type").forGetter(Ticket::getType), ExtraCodecs.NON_NEGATIVE_INT
/* 13 */         .fieldOf("level").forGetter(Ticket::getTicketLevel), Codec.LONG
/* 14 */         .optionalFieldOf("ticks_left", Long.valueOf(0L)).forGetter(()))
/* 15 */       .apply(i, Ticket::new));
/*    */   
/*    */   private final TicketType type;
/*    */   
/*    */   private final int ticketLevel;
/*    */   private long ticksLeft;
/*    */   
/* 22 */   public Ticket(TicketType type, int ticketLevel) { this(type, ticketLevel, type.timeout()); }
/*    */ 
/*    */   
/*    */   private Ticket(TicketType type, int ticketLevel, long ticksLeft) {
/* 26 */     this.type = type;
/* 27 */     this.ticketLevel = ticketLevel;
/* 28 */     this.ticksLeft = ticksLeft;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public String toString() { return this.type.hasTimeout() ? ("Ticket[" + 
/* 34 */       Util.getRegisteredName(BuiltInRegistries.TICKET_TYPE, this.type) + " " + this.ticketLevel + "] with " + this.ticksLeft + " ticks left ( out of" + this.type.timeout() + ")") : ("Ticket[" + 
/*    */       
/* 36 */       Util.getRegisteredName(BuiltInRegistries.TICKET_TYPE, this.type) + " " + this.ticketLevel + "] with no timeout"); }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public TicketType getType() { return this.type; }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public int getTicketLevel() { return this.ticketLevel; }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public void resetTicksLeft() { this.ticksLeft = this.type.timeout(); }
/*    */ 
/*    */   
/*    */   public void decreaseTicksLeft() {
/* 52 */     if (this.type.hasTimeout()) {
/* 53 */       this.ticksLeft--;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 58 */   public boolean isTimedOut() { return (this.type.hasTimeout() && this.ticksLeft < 0L); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\Ticket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */