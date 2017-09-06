# qj-analyzer-plugin



# 사용법

1) 프로젝트 메이븐 빌드
mvn package

2) 서버에 플러그인 파일 업로드


3) 엘라스틱서치 설치
<pre><code>
bin/elasticsearch-plugin install file://`pwd`/qj-analyzer-plugin-1.0.zip
<pre><code>
4) 분석기 세팅
</code></pre>
{

      "analysis":{
        "analyzer":{
          "my-jaso":{
            "type":"custom",
            "tokenizer":"keyword",
            "filter":"my-jaso-filter"
          },
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
                "my-jaso-filter" : {
                    "type" : "qj-analyzer-filter",
                    "tokenizer": "keyword",
                    "mode":"jaso",
                    "jaso_mode" : "full",
                    "jaso_typo" : true
                },
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
5) 옵션 설명

mode : jaso(자동완성용 자소분해기),chosung(초성검색용 초성추출기),soudex(사운덱스 처리용 필터),typo(자판 오타교정 필터)  네가지 모드로 사용 가능하다.

if mode : jaso

   jaso_mode : edge,full (좌단, 전체)
   
   예) 삼성전자
   
   edge : ㅅ 사 삼 삼ㅅ 삼서 삼성 삼성ㅈ 삼성저 삼성전 삼성젅 삼성전ㅈ 삼성전자
   
   full : ㅅ 사 삼 삼ㅅ 삼서 삼성 삼성ㅈ 삼성저 삼성전 삼성젅 삼성전ㅈ 삼성전자 ㅅ 서 성 성ㅈ 성저 성전 성전자 ㅈ 저 전 젅 전ㅈ 전자 ㅈ 자 
   
   jaso_typo : true false (영문 오타어 출력)
   
   
