/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Arrays;
/*    */ import java.util.Locale;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.Heightmap.Types;
/*    */ 
/*    */ public class HeightmapTypeArgument
/*    */   extends StringRepresentableArgument<Heightmap.Types> {
/* 14 */   private static final Codec<Heightmap.Types> LOWER_CASE_CODEC = StringRepresentable.fromEnumWithMapping(HeightmapTypeArgument::keptTypes, s -> s.toLowerCase(Locale.ROOT));
/*    */ 
/*    */   
/* 17 */   private static Heightmap.Types[] keptTypes() { return (Types[])Arrays.stream(Heightmap.Types.values()).filter(Heightmap.Types::keepAfterWorldgen).toArray(x$0 -> new Heightmap.Types[x$0]); }
/*    */ 
/*    */ 
/*    */   
/* 21 */   private HeightmapTypeArgument() { super(LOWER_CASE_CODEC, HeightmapTypeArgument::keptTypes); }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public static HeightmapTypeArgument heightmap() { return new HeightmapTypeArgument(); }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public static Heightmap.Types getHeightmap(CommandContext<CommandSourceStack> context, String name) { return (Heightmap.Types)context.getArgument(name, Heightmap.Types.class); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   protected String convertId(String id) { return id.toLowerCase(Locale.ROOT); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\HeightmapTypeArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */