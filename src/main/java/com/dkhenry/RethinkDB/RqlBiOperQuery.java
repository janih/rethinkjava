package com.dkhenry.RethinkDB;

import com.rethinkdb.Ql2.Term;
import com.rethinkdb.Ql2.Term.TermType;

public abstract class RqlBiOperQuery extends RqlQuery {

	public static class Uuid extends RqlBiOperQuery {
		public Uuid(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.UUID ;
		}
	}
	
	public static class Http extends RqlBiOperQuery {
		public Http(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.HTTP ;
		}
	}
	
	public static class Floor extends RqlBiOperQuery {
		public Floor(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.FLOOR ;
		}
	}
	
	public static class Ceil extends RqlBiOperQuery {
		public Ceil(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.CEIL ;
		}
	}
	
	public static class Round extends RqlBiOperQuery {
		public Round(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.ROUND ;
		}
	}
	
	public static class OffsetsOf extends RqlBiOperQuery {
		public OffsetsOf(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.OFFSETS_OF ;
		}
	}
	
	public static class Valuesarr extends RqlBiOperQuery {
		public Valuesarr(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.VALUESARR ;
		}
	}	
	
	public static class BetweenDeprecated extends RqlBiOperQuery {
		public BetweenDeprecated(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.BETWEEN_DEPRECATED ;
		}
	}
	
	public static class Bracket extends RqlBiOperQuery {
		public Bracket(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.BRACKET ;
		}
	}
	
	public static class Status extends RqlBiOperQuery {
		public Status(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.STATUS ;
		}
	}
	
	public static class Wait extends RqlBiOperQuery {
		public Wait(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.WAIT ;
		}
	}
	
	public static class Reconfigure extends RqlBiOperQuery {
		public Reconfigure(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.RECONFIGURE ;
		}
	}
	
	public static class IndexRename extends RqlBiOperQuery {
		public IndexRename(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.INDEX_RENAME ;
		}
	}
	
	public static class Or extends RqlBiOperQuery {
		public Or(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.OR ;
		}
	}
	
	public static class And extends RqlBiOperQuery {
		public And(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.AND ;
		}
	}
	
	public static class ToJsonString extends RqlBiOperQuery {
		public ToJsonString(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.TO_JSON_STRING;
		}
	}
	
	public static class Changes extends RqlBiOperQuery {
		public Changes(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.CHANGES;
		}
	}
	
	public static class Args extends RqlBiOperQuery {
		public Args(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.ARGS;
		}
	}
	
	public static class Binary extends RqlBiOperQuery {
		public Binary(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.BINARY;
		}
	}
	
	public static class Geojson extends RqlBiOperQuery {
		public Geojson(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.GEOJSON;
		}
	}
	
	public static class ToGeojson extends RqlBiOperQuery {
		public ToGeojson(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.TO_GEOJSON;
		}
	}	
	
	public static class Point extends RqlBiOperQuery {
		public Point(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.POINT;
		}
	}
	
	public static class Line extends RqlBiOperQuery {
		public Line(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.LINE;
		}
	}
	
	public static class Polygon extends RqlBiOperQuery {
		public Polygon(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.POLYGON;
		}
	}	
	
	public static class Distance extends RqlBiOperQuery {
		public Distance(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.DISTANCE;
		}
	}
	
	public static class Intersects extends RqlBiOperQuery {
		public Intersects(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.INTERSECTS;
		}
	}
	
	public static class Includes extends RqlBiOperQuery {
		public Includes(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.INCLUDES;
		}
	}	
	
	public static class Circle extends RqlBiOperQuery {
		public Circle(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.CIRCLE;
		}
	}
	
	public static class GetIntersecting extends RqlBiOperQuery {
		public GetIntersecting(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.GET_INTERSECTING;
		}
	}
	
	public static class GetNearest extends RqlBiOperQuery {
		public GetNearest(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.GET_NEAREST;
		}
	}
	
	public static class PolygonSub extends RqlBiOperQuery {
		public PolygonSub(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.POLYGON_SUB;
		}
	}
	
	public static class Minval extends RqlBiOperQuery {
		public Minval(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.MINVAL;
		}
	}
	
	public static class Maxval extends RqlBiOperQuery {
		public Maxval(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.MAXVAL;
		}
	}	
	
	public static class Fill extends RqlBiOperQuery {
		public Fill(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.FILL;
		}
	}	
	
	public static class Rebalance extends RqlBiOperQuery {
		public Rebalance(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.REBALANCE ;
		}
	}	
	
	public static class Config extends RqlBiOperQuery {
		public Config(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.CONFIG ;
		}
	}	
	
	public static class Range extends RqlBiOperQuery {
		public Range(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.RANGE ;
		}
	}	
	
	public static class Eq extends RqlBiOperQuery {
		public Eq(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.EQ ;
		}
	}

	public static class Ne extends RqlBiOperQuery {
		public Ne(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.NE ;
		}
	}

	public static class Lt extends RqlBiOperQuery {
		public Lt(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.LT ;
		}
	}

	public static class Le extends RqlBiOperQuery {
		public Le(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.LE ;
		}
	}

	public static class Gt extends RqlBiOperQuery {
		public Gt(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.GT ;
		}
	}

	public static class Ge extends RqlBiOperQuery {
		public Ge(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.GE ;
		}
	}
	public static class Add extends RqlBiOperQuery {
		public Add(Object ...args) { 
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.ADD;
		}
	}

	public static class Sub extends RqlBiOperQuery {
		public Sub(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.SUB;
		}
	}

	public static class Mul extends RqlBiOperQuery {
		public Mul(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.MUL ;
		}
	}

	public static class Div extends RqlBiOperQuery {
		public Div(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.DIV ;
		}
	}

	public static class Mod extends RqlBiOperQuery {
		public Mod(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.MOD ;
		}
	}
/*
	public static class Any extends RqlBiOperQuery {
		public Any(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.ANY ;
		}
	}

	public static class All extends RqlBiOperQuery {
		public All(Object ...args) {
			construct(args);
		}
		@Override
		public TermType tt() {
			return Term.TermType.ALL ;
		}
	}
*/
}
