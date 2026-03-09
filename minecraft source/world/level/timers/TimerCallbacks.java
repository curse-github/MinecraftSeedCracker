/*    */ package net.minecraft.world.level.timers;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class TimerCallbacks<C>
/*    */   extends Object
/*    */ {
/* 13 */   public static final TimerCallbacks<MinecraftServer> SERVER_CALLBACKS = (new TimerCallbacks())
/* 14 */     .register(Identifier.withDefaultNamespace("function"), FunctionCallback.CODEC)
/* 15 */     .register(Identifier.withDefaultNamespace("function_tag"), FunctionTagCallback.CODEC);
/*    */   
/* 17 */   private final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends TimerCallback<C>>> idMapper = new ExtraCodecs.LateBoundIdMapper();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   private final Codec<TimerCallback<C>> codec = this.idMapper.codec(Identifier.CODEC).dispatch("Type", TimerCallback::codec, Function.identity());
/*    */ 
/*    */   
/*    */   public TimerCallbacks<C> register(Identifier id, MapCodec<? extends TimerCallback<C>> codec) {
/* 27 */     this.idMapper.put(id, codec);
/* 28 */     return this;
/*    */   }
/*    */ 
/*    */   
/* 32 */   public Codec<TimerCallback<C>> codec() { return this.codec; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\timers\TimerCallbacks.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */