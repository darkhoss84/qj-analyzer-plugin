# qj-analyzer-plugin



# 사용법

1) 프로젝트 메이븐 빌드
mvn package

2) 서버에 플러그인 파일 업로드


3) 엘라스틱서치 설치
<pre><code>
bin/elasticsearch-plugin install file://`pwd`/qj-analyzer-plugin-1.0.zip
</code></pre>

4) 자동완성 분석기 세팅
<pre><code>
{
  "settings" : {
    "index":
    {
      "analysis":{
        "analyzer":{
          "my-index-edge-jaso":{
            "type":"custom",
            "tokenizer":"keyword",
            "filter":["my-jaso-filter","edge_filter"]
          },
          "my-index-full-jaso":{
            "type":"custom",
            "tokenizer":"keyword",
            "filter":["edge_reverse_filter","my-jaso-filter","edge_filter"]
          },
         "my-search-jaso":{
            "type":"custom",
            "tokenizer":"keyword",
            "filter":["my-jaso-filter"]
          }
        },
        "filter" : {
                "my-jaso-filter" : {
                    "type" : "qj-analyzer-filter",
                    "tokenizer": "keyword",
                    "mode":"simple_jaso",
                    "jaso_typo" : true
                },
                "edge_filter": {
                  "type": "edge_ngram",
                  "min_gram": 1,
                  "max_gram": 10,
                  "token_chars": [
                    "letter",
                    "digit"
                  ]
                },
            "edge_reverse_filter": {
                  "type": "edge_ngram",
                  "min_gram": 1,
                  "max_gram": 10,
                  "side" : "back",
                  "token_chars": [
                    "letter",
                    "digit"
                  ]
                }
            }
      }
    }
  },
  "mappings": {
      "article" : {
        "properties" : {
          "title" : {
            "type" : "text",
            "fields": {
                "raw": {
                "type":  "keyword"
              },
              "spell_edge" : {
                "type" : "text",
                "analyzer": "my-index-edge-jaso",
                "search_analyzer":"my-search-jaso"
              },
              "spell_full" : {
                "type" : "text",
                "analyzer": "my-index-full-jaso",
                "search_analyzer":"my-search-jaso"
              }
            }
          }
        }
      }
  }
}
</code></pre>


옵션 설명
mode : simple_jaso(자동완성용 자소분해기)
jaso_mode : true (영문 오타 추출)

spell_edge  : 좌일치 단어
spell_full  : 중간일치 단어도 포함

두 필드를 동시 검색하여 랭킹을 적용하면 좋다.



5) 기타 분석기 세팅
<pre><code>
{

      "analysis":{
        "analyzer":{
          "my-soundex":{
            "type":"custom",
            "tokenizer":"keyword",
            "filter":"my-soundex-filter"
          },
          "my-typo":{
            "type":"custom",
            "tokenizer":"keyword",
            "filter":"my-typo-filter"
          },
          "my-chosung":{
            "type":"custom",
            "tokenizer":"keyword",
            "filter":"my-chosung-filter"
          }
        },
        "filter" : {
                "my-soundex-filter" : {
                    "type" : "qj-analyzer-filter",
                    "tokenizer": "keyword",
                    "mode":"soundex",
                    "jaso_mode" : "full",
                    "jaso_typo" : true
                },
                "my-typo-filter" : {
                    "type" : "qj-analyzer-filter",
                    "tokenizer": "keyword",
                    "mode":"typo",
                    "jaso_mode" : "full",
                    "jaso_typo" : true
                },
                "my-chosung-filter" : {
                    "type" : "qj-analyzer-filter",
                    "tokenizer": "keyword",
                    "mode":"chosung",
                    "jaso_mode" : "full",
                    "jaso_typo" : true
                }
            }
      }

}
</code></pre>

mode : chosung(초성검색용 초성추출기),soudex(사운덱스 처리용 필터),typo(자판 오타교정 필터)


   
