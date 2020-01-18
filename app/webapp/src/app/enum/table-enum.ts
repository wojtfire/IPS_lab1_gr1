export enum TableEnum {
  TAGS = "TAGS",
  MOVIES = "MOVIES",
  RATINGS = "RATINGS",
  LINKS = "LINKS",
  GENOME_TAGS = "GENOME_TAGS",
  GENOME_SCORES = "GENOME_SCORES"
}

export const getTableEnum = (value: string): TableEnum => {
  switch (value) {
    case TableEnum.TAGS:
      return TableEnum.TAGS;
    case TableEnum.MOVIES:
      return TableEnum.MOVIES;
    case TableEnum.RATINGS:
      return TableEnum.RATINGS;
    case TableEnum.LINKS:
      return TableEnum.LINKS;
    case TableEnum.GENOME_TAGS:
      return TableEnum.GENOME_TAGS;
    default:
      return TableEnum.GENOME_SCORES;
  }
};
