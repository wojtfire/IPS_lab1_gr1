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
    case TableEnum.TAGS.toString():
      return TableEnum.TAGS;
    case TableEnum.MOVIES.toString():
      return TableEnum.MOVIES;
    case TableEnum.RATINGS.toString():
      return TableEnum.RATINGS;
    case TableEnum.LINKS.toString():
      return TableEnum.LINKS;
    case TableEnum.GENOME_TAGS.toString():
      return TableEnum.GENOME_TAGS;
    default:
      return TableEnum.GENOME_SCORES;
  }
};
